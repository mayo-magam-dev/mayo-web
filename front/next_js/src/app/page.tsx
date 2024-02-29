"use client";
import { useEffect } from "react";
import { userToken } from "../states/index";
import { useRouter } from "next/navigation";
import { useRecoilState } from "recoil";
import { replaceRouterMain } from "@/utils/RouteHandling";
import { hasToken } from "@/utils/validate/ExistenceChecker";

export default function Home() {
  return <></>;
}
