"use client";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import NewOrder from "./newOrder";
import NewOrderDetailModal from "../atom/newOrderDetailModal";
import { usePathname } from "next/navigation";

export default function NewOrders() {
  const router = useRouter();

  const pathName: string = usePathname();
  let id: string | undefined;

  if (typeof pathName === "string") {
    id = pathName.split("/").pop();
  } else {
    // pathName이 undefined인 경우에 대한 처리
    id = undefined;
  }

  const [selectedNewOrderId, setSelectedNewOrderId] = useState<string | null>(
    null
  );

  return (
    <div className="text-[#F0BD22] ">
      <div className="flex items-center h-[8rem] text-7xl font-bold pl-[2rem] bg-[#25272B]">
        신규 1건
      </div>
      <div className="h-[25rem] overflow-y-auto font-bold">
        {Array.from({ length: 20 }, (_, index) => (
          <div
            onClick={() => router.push("/mayo/processing/" + id + "/new/" + id)}
          >
            <NewOrder key={index} newOrderId={String(index)} />
          </div>
        ))}
      </div>
    </div>
  );
}
