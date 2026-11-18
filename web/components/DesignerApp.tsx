"use client";

import { useCallback, useEffect, useRef, useState, type ReactNode } from "react";
import { filterShots, PAGE_SIZE } from "@/lib/shots";
import { readCachedShots, readUser, writeCachedShots, writeUser } from "@/lib/storage";
import type { DesignerUser, Shot } from "@/lib/types";
import { Drawer, type DrawerView } from "./Drawer";
import { Icon } from "./icons";
import { LoginScreen } from "./LoginScreen";
import { ShotCard } from "./ShotCard";
import { ShotViewer } from "./ShotViewer";

type Screen = "main" | "login" | "shot" | "person";
type ShotSource = "feed" | "search" | "likes";

async function fetchShots(page: number, perPage: number, query = ""): Promise<Shot[]> {
  const params = new URLSearchParams({
    page: String(page),
    per_page: String(perPage),
  });
  if (query) params.set("q", query);
  const response = await fetch(`/api/shots?${params.toString()}`);
  if (!response.ok) throw new Error("network");
  return (await response.json()) as Shot[];
}

function userFromName(username: string): DesignerUser {
  let hash = 0;
  for (let i = 0; i < username.length; i += 1) {
    hash = (hash * 31 + username.charCodeAt(i)) >>> 0;
  }
  return {
    id: hash || 1,
    name: username,
    username,
    html_url: `https://dribbble.com/${encodeURIComponent(username)}`,
    avatar_url: `https://api.dicebear.com/9.x/adventurer/svg?seed=${encodeURIComponent(username)}`,
  };
}

export default function DesignerApp() {
  const [screen, setScreen] = useState<Screen>("main");
  const [view, setView] = useState<DrawerView>("feed");
  const [drawerOpen, setDrawerOpen] = useState(false);
  const [shots, setShots] = useState<Shot[]>([]);
  const [catalog, setCatalog] = useState<Shot[] | null>(null);
  const [query, setQuery] = useState("");
  const [liked, setLiked] = useState<Set<number>>(() => new Set());
  const [user, setUser] = useState<DesignerUser | null>(null);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [loadingMore, setLoadingMore] = useState(false);
  const [hasMore, setHasMore] = useState(true);
  const [shotIndex, setShotIndex] = useState(0);
  const [shotSource, setShotSource] = useState<ShotSource>("feed");
  const [toast, setToast] = useState<string | null>(null);
  const [menuOpen, setMenuOpen] = useState(false);
  const [pull, setPull] = useState(0);
  const toastTimer = useRef<number | null>(null);
  const shotsRef = useRef<Shot[]>([]);
  const hasMoreRef = useRef(true);
  const loadingMoreRef = useRef(false);
  const menuRef = useRef<HTMLDivElement>(null);
  const scrollerRef = useRef<HTMLDivElement>(null);
  const pullStart = useRef<number | null>(null);
  const pullRef = useRef(0);

  shotsRef.current = shots;
  hasMoreRef.current = hasMore;

  const showToast = useCallback((message: string) => {
    setToast(message);
    if (toastTimer.current) window.clearTimeout(toastTimer.current);
    toastTimer.current = window.setTimeout(() => setToast(null), 2200);
  }, []);

  const loadMore = useCallback(async () => {
    if (loadingMoreRef.current || !hasMoreRef.current) return;
    loadingMoreRef.current = true;
    setLoadingMore(true);
    try {
      const nextPage = Math.floor(shotsRef.current.length / PAGE_SIZE) + 1;
      const fresh = await fetchShots(nextPage, PAGE_SIZE);
      if (!fresh.length) {
        setHasMore(false);
      } else {
        setShots((current) => {
          const ids = new Set(current.map((shot) => shot.id));
          const extra = fresh.filter((shot) => !ids.has(shot.id));
          return extra.length ? [...current, ...extra] : current;
        });
        setHasMore(fresh.length === PAGE_SIZE);
      }
    } catch {
      showToast("网络刷新出现了错误");
    } finally {
      loadingMoreRef.current = false;
      setLoadingMore(false);
    }
  }, [showToast]);

  const refresh = useCallback(async () => {
    setRefreshing(true);
    try {
      const fresh = await fetchShots(1, PAGE_SIZE);
      const current = shotsRef.current;
      if (current.length && fresh[0] && current[0].id === fresh[0].id) {
        showToast("已经是最新的数据了");
      } else if (fresh.length) {
        setShots(fresh);
        writeCachedShots(fresh);
        setHasMore(fresh.length === PAGE_SIZE);
      } else {
        showToast("网络刷新出现了错误");
      }
    } catch {
      showToast("网络刷新出现了错误");
    } finally {
      setRefreshing(false);
    }
  }, [showToast]);

  useEffect(() => {
    const savedUser = readUser();
    if (savedUser) setUser(savedUser);
    const cached = readCachedShots();
    if (cached.length) setShots(cached);

    let cancelled = false;
    async function boot() {
      setLoading(true);
      try {
        const fresh = await fetchShots(1, PAGE_SIZE);
        if (cancelled) return;
        if (cached.length && fresh[0] && cached[0]?.id === fresh[0].id) {
          showToast("已经是最新的数据了");
          setHasMore(true);
        } else if (fresh.length) {
          setShots(fresh);
          writeCachedShots(fresh);
          setHasMore(fresh.length === PAGE_SIZE);
        } else if (!cached.length) {
          showToast("网络刷新出现了错误");
        }
      } catch {
        if (!cancelled) showToast("网络刷新出现了错误");
      } finally {
        if (!cancelled) setLoading(false);
      }
    }
    void boot();
    return () => {
      cancelled = true;
    };
  }, [showToast]);

  useEffect(() => {
    if (view !== "search" || catalog) return;
    let cancelled = false;
    fetchShots(1, 50)
      .then((all) => {
        if (!cancelled) setCatalog(all);
      })
      .catch(() => {
        if (!cancelled) showToast("网络刷新出现了错误");
      });
    return () => {
      cancelled = true;
    };
  }, [catalog, showToast, view]);

  useEffect(() => {
    if (screen !== "shot" || shotSource !== "feed") return;
    if (shotIndex >= shots.length - 1) void loadMore();
  }, [loadMore, screen, shotIndex, shotSource, shots.length]);

  useEffect(() => {
    if (!menuOpen) return;
    function onDoc(event: MouseEvent) {
      if (!menuRef.current?.contains(event.target as Node)) setMenuOpen(false);
    }
    document.addEventListener("mousedown", onDoc);
    return () => document.removeEventListener("mousedown", onDoc);
  }, [menuOpen]);

  useEffect(() => {
    if (screen === "shot" || screen === "login") return;
    function onKey(event: KeyboardEvent) {
      if (event.key === "Escape") setDrawerOpen(false);
    }
    window.addEventListener("keydown", onKey);
    return () => window.removeEventListener("keydown", onKey);
  }, [screen]);

  const searchPool = query.trim() ? (catalog ?? shots) : shots;
  const searchResults = filterShots(searchPool, query);
  const likedShots = shots.filter((shot) => liked.has(shot.id));
  const visible = view === "search" ? searchResults : view === "likes" ? likedShots : shots;
  const viewerShots =
    shotSource === "search" ? searchResults : shotSource === "likes" ? likedShots : shots;
  const titles: Record<DrawerView, string> = {
    feed: "设计狮",
    search: "搜索",
    likes: "我的喜欢",
    settings: "设置",
  };

  function openShot(source: ShotSource, index: number) {
    setShotSource(source);
    setShotIndex(index);
    setScreen("shot");
  }

  function toggleLike(id: number) {
    setLiked((current) => {
      const next = new Set(current);
      if (next.has(id)) next.delete(id);
      else next.add(id);
      return next;
    });
  }

  async function shareLink(title: string, url: string) {
    try {
      if (navigator.share) {
        await navigator.share({ title, url });
        return;
      }
    } catch (error) {
      if (error instanceof DOMException && error.name === "AbortError") return;
    }
    try {
      await navigator.clipboard.writeText(url);
      showToast("已复制链接");
    } catch {
      showToast(title);
    }
  }

  function go(next: DrawerView) {
    setView(next);
    setScreen("main");
    setDrawerOpen(false);
    setMenuOpen(false);
    scrollerRef.current?.scrollTo({ top: 0 });
  }

  const drawer = (
    <Drawer
      open={drawerOpen}
      active={view}
      user={user}
      onClose={() => setDrawerOpen(false)}
      onNavigate={go}
      onAvatar={() => {
        setDrawerOpen(false);
        if (user) setScreen("person");
        else setScreen("login");
      }}
      onShare={() => {
        setDrawerOpen(false);
        void shareLink("设计狮", window.location.origin);
      }}
    />
  );

  if (screen === "login") {
    return (
      <Phone toast={toast}>
        <LoginScreen
          onBack={() => {
            showToast("你还没有成功登陆哟！！！");
            setScreen("main");
          }}
          onSubmit={(username) => {
            const next = userFromName(username);
            writeUser(next);
            setUser(next);
            setScreen("main");
            setDrawerOpen(true);
          }}
        />
      </Phone>
    );
  }

  if (screen === "shot") {
    const safeIndex = Math.min(shotIndex, Math.max(viewerShots.length - 1, 0));
    const current = viewerShots[safeIndex];
    return (
      <Phone toast={toast}>
        <ShotViewer
          shots={viewerShots}
          index={safeIndex}
          liked={current ? liked.has(current.id) : false}
          onIndex={setShotIndex}
          onBack={() => {
            const id = current?.id;
            setScreen("main");
            if (id == null) return;
            window.setTimeout(() => {
              document.getElementById(`shot-${id}`)?.scrollIntoView({ block: "center" });
            }, 0);
          }}
          onToggleLike={() => {
            if (current) toggleLike(current.id);
          }}
          onShare={() => {
            if (!current) return;
            void shareLink(current.title, current.html_url);
          }}
        />
      </Phone>
    );
  }

  const listSource: ShotSource = view === "search" ? "search" : view === "likes" ? "likes" : "feed";

  return (
    <Phone toast={toast}>
      {screen === "person" && user ? (
        <section className="person">
          <header className="toolbar">
            <button type="button" className="icon-button" aria-label="打开" onClick={() => setDrawerOpen(true)}>
              <Icon name="menu" />
            </button>
            <h1>{user.username}</h1>
          </header>
          <div className="person-body">
            <img src={user.avatar_url} alt="" />
            <p>{user.username}</p>
            <p className="person-url">{user.html_url}</p>
          </div>
        </section>
      ) : (
        <>
          <header className="toolbar">
            <button type="button" className="icon-button" aria-label="打开" onClick={() => setDrawerOpen(true)}>
              <Icon name="menu" />
            </button>
            <h1>{titles[view]}</h1>
            <div className="toolbar-actions" ref={menuRef}>
              <button type="button" className="icon-button" aria-label="搜索" onClick={() => go("search")}>
                <Icon name="search" />
              </button>
              <button
                type="button"
                className="icon-button"
                aria-label="更多"
                aria-expanded={menuOpen}
                onClick={() => setMenuOpen((open) => !open)}
              >
                <Icon name="more" />
              </button>
              {menuOpen ? (
                <div className="menu-pop">
                  <button type="button" onClick={() => go("settings")}>
                    设置
                  </button>
                </div>
              ) : null}
            </div>
          </header>

          {view === "search" ? (
            <form className="search-bar" onSubmit={(event) => event.preventDefault()}>
              <Icon name="search" size={20} />
              <input
                value={query}
                onChange={(event) => setQuery(event.target.value)}
                placeholder="搜索"
                aria-label="搜索"
                autoFocus
              />
            </form>
          ) : null}

          <div
            className="scroller"
            ref={scrollerRef}
            onScroll={(event) => {
              if (view !== "feed") return;
              const el = event.currentTarget;
              if (el.scrollHeight - el.scrollTop - el.clientHeight < 160) void loadMore();
            }}
            onPointerDown={(event) => {
              if (view !== "feed" || event.currentTarget.scrollTop > 0) return;
              pullStart.current = event.clientY;
            }}
            onPointerMove={(event) => {
              if (pullStart.current == null || view !== "feed") return;
              const dy = event.clientY - pullStart.current;
              if (dy > 12 && event.currentTarget.scrollTop <= 0) {
                const next = Math.min(72, (dy - 12) * 0.4);
                pullRef.current = next;
                setPull(next);
              }
            }}
            onPointerUp={() => {
              if (pullRef.current >= 56) void refresh();
              pullStart.current = null;
              pullRef.current = 0;
              setPull(0);
            }}
            onPointerCancel={() => {
              pullStart.current = null;
              pullRef.current = 0;
              setPull(0);
            }}
          >
            {view === "feed" && (refreshing || pull > 0 || (loading && shots.length === 0)) ? (
              <div className="refresh-row">
                <div className="spinner" />
              </div>
            ) : null}

            {view === "settings" ? (
              <section className="settings">
                <p>
                  <span>应用</span>
                  <strong>设计狮</strong>
                </p>
                <p>
                  <span>版本</span>
                  <strong>1.0</strong>
                </p>
              </section>
            ) : null}

            {view !== "settings" && visible.length === 0 && !loading ? (
              <p className="empty">
                {view === "likes" ? "还没有喜欢的作品" : view === "search" ? "没有匹配的作品" : "网络刷新出现了错误"}
              </p>
            ) : null}

            {view === "search" && query.trim() && !catalog && visible.length === 0 ? (
              <div className="refresh-row">
                <div className="spinner" />
              </div>
            ) : null}

            {view !== "settings"
              ? visible.map((shot, index) => (
                  <ShotCard
                    key={shot.id}
                    shot={shot}
                    liked={liked.has(shot.id)}
                    onOpen={() => openShot(listSource, index)}
                    onToggleLike={() => toggleLike(shot.id)}
                    onShare={() => void shareLink(shot.title, shot.html_url)}
                  />
                ))
              : null}

            {view === "feed" && loadingMore ? (
              <div className="refresh-row">
                <div className="spinner" />
              </div>
            ) : null}
          </div>
        </>
      )}
      {drawer}
    </Phone>
  );
}

function Phone({ toast, children }: { toast: string | null; children: ReactNode }) {
  return (
    <div className="stage">
      <div className="phone">
        {children}
        {toast ? (
          <div className="toast" role="status">
            {toast}
          </div>
        ) : null}
      </div>
    </div>
  );
}
