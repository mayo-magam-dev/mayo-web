"use client";
import NewOrderDetailModal from "@/atoms/atom/newOrderDetailModal";
// http://localhost:3000/mayo/processing/Asb0QBuQ9gmEyyWlSVhY
import MayoHeader from "@/atoms/molecule/mayoHeader";
import MayoProcessingMain from "@/atoms/molecule/mayoProcessingMain";
import NavigationLeft from "@/atoms/molecule/navigationLeft";
import NewOrder from "@/atoms/molecule/newOrders";
import { usePathname } from "next/navigation";
export default function MayoProcessing() {
  const pathName: string = usePathname();
  let id: string | undefined;

  if (typeof pathName === "string") {
    id = pathName.split("/").pop();
  } else {
    // pathName이 undefined인 경우에 대한 처리
    id = undefined;
  }
  const cssCommen =
    "flex justify-center items-center h-[13rem] w-[70rem] bg-[#25272B] m-[10rem]";
  return (
    <main className="rounded-[2rem] w-[95%] h-[100rem] m-[5rem]">
      <header>
        <MayoHeader />
      </header>
      <div className="flex ">
        <NavigationLeft id={id} />
      </div>
    </main>
  );
}
