import type { DesignerUser, Shot } from "./types";

const SHOTS_KEY = "designer.shots.v1";
const USER_KEY = "designer.user.v1";

export function readCachedShots(): Shot[] {
  const parsed = readJson<unknown>(SHOTS_KEY);
  return Array.isArray(parsed) ? (parsed as Shot[]) : [];
}

export function writeCachedShots(shots: Shot[]) {
  writeJson(SHOTS_KEY, shots.slice(0, 10));
}

export function readUser(): DesignerUser | null {
  return readJson<DesignerUser>(USER_KEY);
}

export function writeUser(user: DesignerUser) {
  writeJson(USER_KEY, user);
}

function readJson<T>(key: string): T | null {
  try {
    const raw = localStorage.getItem(key);
    if (!raw) return null;
    return JSON.parse(raw) as T;
  } catch {
    return null;
  }
}

function writeJson(key: string, value: unknown) {
  localStorage.setItem(key, JSON.stringify(value));
}
