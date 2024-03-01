"use client";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import NewOrder from "../molecule/newOrder";

interface IteminfoDetailModalProps {
  selectedItemInfoId: string | null;
}

export default function IteminfoDetailModal(props: IteminfoDetailModalProps) {
  return (
    <div className=" absolute top-[20rem] left-[105rem] flex flex-col justify-center text-black text-5xl h-[90rem] w-[128rem] ">
      sadfsdf {props.selectedItemInfoId}
    </div>
  );
}
