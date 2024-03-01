"use client";
import NewOrderDetailModal from "@/atoms/atom/newOrderDetailModal";
import ManageMenu from "@/atoms/molecule/manageMenu";
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
  return (
    <main className="rounded-[2rem] w-[95%] h-[100rem] m-[5rem] ">sdfs</main>
  );
}
