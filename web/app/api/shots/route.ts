import { NextRequest, NextResponse } from "next/server";
import { getShotsPage } from "@/lib/shots";

export function GET(request: NextRequest) {
  const page = Number(request.nextUrl.searchParams.get("page") ?? "1");
  const perPage = Number(request.nextUrl.searchParams.get("per_page") ?? "10");
  const query = request.nextUrl.searchParams.get("q") ?? "";
  return NextResponse.json(getShotsPage(page, perPage, query));
}
