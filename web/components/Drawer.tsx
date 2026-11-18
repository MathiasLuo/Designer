import type { DesignerUser } from "@/lib/types";
import { Icon } from "./icons";

export type DrawerView = "feed" | "search" | "likes" | "settings";

const MAIN_ITEMS: Array<{ id: DrawerView; label: string; icon: string }> = [
  { id: "feed", label: "设计狮", icon: "home" },
  { id: "search", label: "搜索", icon: "search" },
  { id: "likes", label: "我的喜欢", icon: "bookmark" },
];

export function Drawer({
  open,
  active,
  user,
  onClose,
  onNavigate,
  onAvatar,
  onShare,
}: {
  open: boolean;
  active: DrawerView;
  user: DesignerUser | null;
  onClose: () => void;
  onNavigate: (view: DrawerView) => void;
  onAvatar: () => void;
  onShare: () => void;
}) {
  return (
    <div className={open ? "drawer-root open" : "drawer-root"} hidden={!open}>
      <button type="button" className="drawer-scrim" aria-label="关闭" onClick={onClose} />
      <nav className="drawer" aria-label="侧栏">
        <div className="drawer-header">
          <button type="button" className="avatar-button" onClick={onAvatar} aria-label={user ? "个人主页" : "点击头像登录"}>
            {user ? (
              <img src={user.avatar_url} alt="" />
            ) : (
              <span className="avatar-fallback">
                <Icon name="person" size={36} />
              </span>
            )}
          </button>
          <p className="drawer-name">{user ? user.username : "点击头像登录"}</p>
          <p className="drawer-desc">{user ? user.html_url : "登陆后更加精彩,你懂得..."}</p>
        </div>
        <div className="drawer-menu">
          {MAIN_ITEMS.map((item) => (
            <button
              key={item.id}
              type="button"
              className={active === item.id ? "nav-item active" : "nav-item"}
              aria-current={active === item.id ? "page" : undefined}
              onClick={() => onNavigate(item.id)}
            >
              <Icon name={item.icon} />
              <span>{item.label}</span>
            </button>
          ))}
          <p className="nav-label">其它</p>
          <button type="button" className="nav-item" onClick={onShare}>
            <Icon name="share" />
            <span>分享</span>
          </button>
          <button
            type="button"
            className={active === "settings" ? "nav-item active" : "nav-item"}
            aria-current={active === "settings" ? "page" : undefined}
            onClick={() => onNavigate("settings")}
          >
            <Icon name="settings" />
            <span>设置</span>
          </button>
        </div>
      </nav>
    </div>
  );
}
