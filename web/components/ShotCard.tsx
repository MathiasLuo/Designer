import type { Shot } from "@/lib/types";
import { Icon } from "./icons";

export function ShotCard({
  shot,
  liked,
  onOpen,
  onToggleLike,
  onShare,
}: {
  shot: Shot;
  liked: boolean;
  onOpen: () => void;
  onToggleLike: () => void;
  onShare: () => void;
}) {
  return (
    <article className="shot-card" id={`shot-${shot.id}`}>
      <button type="button" className="shot-open" onClick={onOpen}>
        <img
          src={shot.images.normal}
          alt={shot.title}
          style={{ aspectRatio: `${shot.width} / ${shot.height}` }}
        />
      </button>
      <div className="shot-author">
        <img className="avatar" src={shot.user.avatar_url} alt="" />
        <span className="username">{shot.user.username}</span>
        <button
          type="button"
          className="heart"
          aria-pressed={liked}
          aria-label={liked ? "取消喜欢" : "喜欢"}
          onClick={onToggleLike}
        >
          <Icon name={liked ? "favorite" : "favorite-outline"} size={28} />
        </button>
      </div>
      <div className="shot-stat">
        <Icon name="favorite" size={20} />
        <span>{shot.likes_count} 人喜欢这张照片。</span>
      </div>
      <div className="shot-stat">
        <Icon name="comment" size={20} />
        <span>{shot.comments_count}</span>
      </div>
      <div className="shot-actions">
        <button type="button" onClick={onOpen}>
          <Icon name="info" size={20} />
          <span>详情</span>
        </button>
        <button type="button" onClick={onShare}>
          <Icon name="share" size={20} />
          <span>分享</span>
        </button>
      </div>
    </article>
  );
}
