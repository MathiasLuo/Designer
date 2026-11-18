import assert from "node:assert/strict";
import { describe, it } from "node:test";
import { CATALOG } from "./catalog";
import { filterShots, getShotsPage } from "./shots";

describe("getShotsPage", () => {
  it("returns the first page of 10", () => {
    const page = getShotsPage(1, 10);
    assert.equal(page.length, 10);
    assert.equal(page[0]?.id, CATALOG[0]?.id);
  });

  it("returns the short last page", () => {
    const page = getShotsPage(3, 10);
    assert.equal(page.length, CATALOG.length - 20);
    assert.equal(page[0]?.id, CATALOG[20]?.id);
  });

  it("returns an empty page past the end", () => {
    assert.deepEqual(getShotsPage(9, 10), []);
  });

  it("treats an invalid page as the first page and clamps the page size", () => {
    assert.equal(getShotsPage(0, 0).length, 10);
    assert.equal(getShotsPage(Number.NaN, 8).length, 8);
    assert.equal(getShotsPage(1, 100).length, CATALOG.length);
  });
});

describe("filterShots", () => {
  it("matches a title or username and returns nothing for an unknown query", () => {
    const title = CATALOG[2]?.title ?? "";
    assert.ok(filterShots(CATALOG, title.slice(0, 6)).some((shot) => shot.id === CATALOG[2]?.id));
    assert.ok(filterShots(CATALOG, "lina.studio").every((shot) => shot.user.username === "lina.studio"));
    assert.deepEqual(filterShots(CATALOG, "zzzz-not-a-shot"), []);
    assert.equal(filterShots(CATALOG, "   ").length, CATALOG.length);
  });
});
