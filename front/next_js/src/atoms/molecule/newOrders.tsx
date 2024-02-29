"use client";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import NewOrder from "./newOrder";

export default function NewOrders() {
  const router = useRouter();

  return (
    <div className="text-[#F0BD22]">
      <div className="flex items-center h-[8rem] text-7xl font-bold pl-[2rem] bg-[#25272B]">
        신규 1건
      </div>
      <div className="h-[25rem] overflow-y-auto">
        <NewOrder />
        <NewOrder />
        <NewOrder />
      </div>
    </div>
  );
}
