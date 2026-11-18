import { useState } from "react";
import { Icon } from "./icons";

export function LoginScreen({
  onBack,
  onSubmit,
}: {
  onBack: () => void;
  onSubmit: (username: string) => void;
}) {
  const [username, setUsername] = useState("");

  return (
    <section className="login">
      <header className="toolbar">
        <button type="button" className="icon-button" onClick={onBack} aria-label="返回">
          <Icon name="back" />
        </button>
        <h1>登陆</h1>
      </header>
      <form
        className="login-body"
        onSubmit={(event) => {
          event.preventDefault();
          const next = username.trim();
          if (!next) return;
          onSubmit(next.slice(0, 32));
        }}
      >
        <p>登陆后更加精彩,你懂得...</p>
        <label>
          用户名
          <input
            value={username}
            onChange={(event) => setUsername(event.target.value)}
            autoComplete="username"
            maxLength={32}
            required
          />
        </label>
        <button type="submit" className="login-submit">
          登录
        </button>
        <p className="login-note">Dribbble 授权已不可用，登录信息只保存在这台设备上。</p>
      </form>
    </section>
  );
}
