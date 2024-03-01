"use client";
import EndOrders from "@/atoms/molecule/endOrders";
import EnrollMenu from "@/atoms/molecule/enrollMenu";
// http://localhost:3000/mayo/processing/Asb0QBuQ9gmEyyWlSVhY
import MayoHeader from "@/atoms/molecule/mayoHeader";
import MayoProcessingMain from "@/atoms/molecule/mayoProcessingMain";
import NavigationLeft from "@/atoms/molecule/navigationLeft";
import NewOrders from "@/atoms/molecule/newOrders";
import NewOrder from "@/atoms/molecule/newOrders";
import ProcessingOrders from "@/atoms/molecule/processingOrders";
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
    <main className="rounded-[2rem] w-[95%] h-[100rem] m-[5rem]">
      <header>
        <MayoHeader />
      </header>
      <div className="flex ">
        <NavigationLeft id={id} />
        <div className="h-[90rem]   w-[95%]">
          <div className="flex justify-center items-center font-bold text-7xl h-[15rem] text-5xl  bg-[#E3E3E3] w-[100%]">
            <div className="w-[25%] flex justify-center">판매 설정</div>
            <div className="w-[25%] flex justify-center">상품명</div>
            <div className="w-[25%] flex justify-center">수량</div>
            <div className="w-[25%] flex justify-center"></div>
          </div>
          <div className="flex  items-center h-[75rem] font-bold text-7xl h-[75rem]  w-[100%] overflow-y-auto ">
            <div>
              {Array.from({ length: 30 }, (_, index) => (
                <EnrollMenu key={index} />
              ))}
            </div>
            <div className="w-[55rem] h-[40rem] flex flex-col justify-center items-center">
              <div className="flex justify-center items-center h-[10rem] w-[40rem] bg-[#F2BE22] rounded-[2rem] mb-[6rem]">
                오픈하기
              </div>
              <div className="flex justify-center items-center h-[10rem] w-[40rem] bg-[#EDEDED] rounded-[2rem]">
                마감하기
              </div>
            </div>
          </div>
        </div>
        <div></div>
      </div>
    </main>
  );
}
