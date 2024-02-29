"use client";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import NewOrders from "./newOrders";
import ProcessingOrders from "./processingOrders";

export default function MayoProcessingMain() {
  const router = useRouter();

  return (
    <div className="h-[90rem] text-6xl font-bold bg-[#0E0E0E] w-[80rem]">
      <NewOrders />
      <ProcessingOrders />
    </div>
  );
}
