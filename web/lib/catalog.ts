import type { Shot } from "./types";

const PHOTOS = [
  "photo-1561070791-2526d30994b5",
  "photo-1558655146-d09347e92766",
  "photo-1626785774573-4b799315345d",
  "photo-1572044162444-ad60f128bdea",
  "photo-1541462608143-67571c6738dd",
  "photo-1609921212029-bb5a28e60960",
  "photo-1618005182384-a83a8bd57fbe",
  "photo-1545235617-9465d2a55698",
  "photo-1523726491678-bf852e717f6a",
  "photo-1498050108023-c5249f4df085",
  "photo-1507238691740-187a5b1d37b8",
  "photo-1460925895917-afdab827c52f",
  "photo-1512295767273-ac109ac3acfa",
  "photo-1586717791821-3f44a563fa4c",
  "photo-1576153192396-180ecef2a715",
  "photo-1618005198919-d3d4b5a92ead",
  "photo-1559028012-481c04fa702d",
  "photo-1542744094-24638eff58bb",
  "photo-1499951360447-b19be8fe80f5",
  "photo-1600132806370-bf17e65e942f",
  "photo-1634942537034-2531766767d1",
  "photo-1561070791-36c11767b26a",
  "photo-1613909207039-6b173b755cc1",
  "photo-1558655146-9f40138edfeb",
];

const AVATARS = [
  "photo-1494790108377-be9c29b29330",
  "photo-1500648767791-00dcc994a43e",
  "photo-1438761681033-6461ffad8d80",
  "photo-1507003211169-0a1dd7228f2d",
  "photo-1544005313-94ddf0286df2",
  "photo-1534528741775-53994a69daeb",
  "photo-1527980965255-d3b416303d12",
  "photo-1547425260-76bcadfb4f2c",
];

const PEOPLE: Array<[string, string]> = [
  ["Lina Chen", "lina.studio"],
  ["Jun Park", "pixel.jun"],
  ["Mara Ortiz", "atelier.m"],
  ["Noah Berg", "northwind"],
  ["Hana Ito", "hoja"],
  ["Eli Grant", "form.lab"],
  ["Solo Liu", "solo.liu"],
  ["Vera Santos", "press.room"],
];

const TITLES = [
  "Morning Brand Kit",
  "Blue Ticket App",
  "Poster Study 04",
  "Dashboard Cards",
  "Cafe Identity",
  "Gradient Icons",
  "Abstract Cover",
  "Editorial Layout",
  "Workspace Notes",
  "Portfolio Hero",
  "Analytics Mobile",
  "Shape Poster",
  "Color Blocks",
  "Landing Wire",
  "Illustration Set",
  "Soft 3D Study",
  "Campaign Colors",
  "Team Workshop",
  "Night Desk",
  "App Onboarding",
  "UI Kit Preview",
  "Mark Sketch",
  "Festival Poster",
  "Packaging Flat",
];

function image(id: string, width: number): string {
  return `https://images.unsplash.com/${id}?auto=format&fit=crop&w=${width}&q=80`;
}

export const CATALOG: Shot[] = PHOTOS.map((photo, index) => {
  const [name, username] = PEOPLE[index % PEOPLE.length];
  const avatar = AVATARS[index % AVATARS.length];
  const tall = index % 5 === 0;
  const width = 800;
  const height = tall ? 1000 : 600;
  const id = 48021001 + index;
  return {
    id,
    title: TITLES[index],
    description: `${TITLES[index]} by ${name}.`,
    width,
    height,
    images: {
      hidpi: image(photo, 1600),
      normal: image(photo, 800),
      teaser: image(photo, 400),
    },
    views_count: 200 + ((index * 53) % 4000),
    likes_count: 80 + ((index * 37) % 900),
    comments_count: 4 + ((index * 13) % 120),
    created_at: `2016-03-${String((index % 27) + 1).padStart(2, "0")}T10:00:00Z`,
    html_url: `https://dribbble.com/shots/${id}`,
    animated: index % 11 === 0,
    user: {
      id: 7000 + (index % PEOPLE.length),
      name,
      username,
      html_url: `https://dribbble.com/${username}`,
      avatar_url: image(avatar, 200),
    },
  };
});
