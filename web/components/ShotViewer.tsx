import { useEffect, useRef, useState } from "react";
import type { Shot } from "@/lib/types";
import { Icon } from "./icons";

export function ShotViewer({
  shots,
  index,
  liked,
  onIndex,
  onBack,
  onToggleLike,
  onShare,
}: {
  shots: Shot[];
  index: number;
  liked: boolean;
  onIndex: (index: number) => void;
  onBack: () => void;
  onToggleLike: () => void;
  onShare: () => void;
}) {
  const shot = shots[index];
  const startX = useRef<number | null>(null);
  const [imageReady, setImageReady] = useState(false);

  useEffect(() => {
    setImageReady(false);
  }, [shot?.id]);

  useEffect(() => {
    function onKey(event: KeyboardEvent) {
      if (event.key === "Escape") onBack();
      if (event.key === "ArrowRight") onIndex(Math.min(shots.length - 1, index + 1));
      if (event.key === "ArrowLeft") onIndex(Math.max(0, index - 1));
    }
    window.addEventListener("keydown", onKey);
    return () => window.removeEventListener("keydown", onKey);
  }, [index, onBack, onIndex, shots.length]);

  if (!shot) return null;

  function step(direction: number) {
    const next = index + direction;
    if (next < 0 || next >= shots.length) return;
    onIndex(next);
  }

  return (
    <section
      className="viewer"
      aria-label={shot.title}
      onPointerDown={(event) => {
        startX.current = event.clientX;
      }}
      onPointerUp={(event) => {
        if (startX.current == null) return;
        const dx = event.clientX - startX.current;
        startX.current = null;
        if (dx <= -48) step(1);
        else if (dx >= 48) step(-1);
      }}
      onPointerCancel={() => {
        startX.current = null;
      }}
    >
      <header className="viewer-bar">
        <button type="button" onClick={onBack} aria-label="返回">
          <Icon name="back" size={22} />
        </button>
        <h1>{shot.title}</h1>
        <button type="button" onClick={onShare} aria-label="分享">
          <Icon name="share" size={22} />
        </button>
      </header>
      <div
        className="viewer-stage"
        onClick={(event) => {
          const bounds = event.currentTarget.getBoundingClientRect();
          const x = event.clientX - bounds.left;
          if (x < bounds.width * 0.28) step(-1);
          else if (x > bounds.width * 0.72) step(1);
        }}
      >
        {!imageReady ? <div className="spinner" /> : null}
        <img
          key={shot.id}
          src={shot.images.normal}
          alt={shot.title}
          onLoad={() => setImageReady(true)}
        />
      </div>
      <footer className="viewer-foot">
        <div className="viewer-user">
          <img src={shot.user.avatar_url} alt="" />
          <span>{shot.user.name}</span>
          <span className="viewer-comments">
            <Icon name="comment" size={18} />
            {shot.comments_count}
          </span>
          <button type="button" className="heart" aria-pressed={liked} aria-label={liked ? "取消喜欢" : "喜欢"} onClick={onToggleLike}>
            <Icon name={liked ? "favorite" : "favorite-outline"} size={22} />
          </button>
        </div>
        <p>
          <Icon name="favorite" size={18} />
          <span>
            {shot.likes_count} 个人喜欢这个照片
          </span>
        </p>
      </footer>
    </section>
  );
}
