import { CATALOG } from "./catalog";
import type { Shot } from "./types";

export const PAGE_SIZE = 10;

export function filterShots(shots: Shot[], query: string): Shot[] {
  const q = query.trim().toLowerCase();
  if (!q) return shots;
  return shots.filter((shot) => {
    const haystack = `${shot.title} ${shot.user.username} ${shot.user.name}`.toLowerCase();
    return haystack.includes(q);
  });
}

export function getShotsPage(page: number, perPage: number, query = ""): Shot[] {
  const filtered = filterShots(CATALOG, query);
  const safePage = Number.isFinite(page) && page >= 1 ? Math.floor(page) : 1;
  const requested = Number.isFinite(perPage) && perPage >= 1 ? Math.floor(perPage) : PAGE_SIZE;
  const size = Math.min(50, requested);
  const start = (safePage - 1) * size;
  return filtered.slice(start, start + size);
}
