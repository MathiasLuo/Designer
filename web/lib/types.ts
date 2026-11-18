export type DesignerUser = {
  id: number;
  name: string;
  username: string;
  html_url: string;
  avatar_url: string;
};

export type Shot = {
  id: number;
  title: string;
  description: string;
  width: number;
  height: number;
  images: {
    hidpi: string;
    normal: string;
    teaser: string;
  };
  views_count: number;
  likes_count: number;
  comments_count: number;
  created_at: string;
  html_url: string;
  animated: boolean;
  user: DesignerUser;
};
