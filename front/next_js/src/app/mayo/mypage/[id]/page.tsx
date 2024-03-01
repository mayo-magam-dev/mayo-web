"use client";
import NewOrderDetailModal from "@/atoms/atom/newOrderDetailModal";
// http://localhost:3000/mayo/processing/Asb0QBuQ9gmEyyWlSVhY
import MayoHeader from "@/atoms/molecule/mayoHeader";
import MayoProcessingMain from "@/atoms/molecule/mayoProcessingMain";
import NavigationLeft from "@/atoms/molecule/navigationLeft";
import NewOrder from "@/atoms/molecule/newOrders";
import { usePathname } from "next/navigation";
import { useRouter } from "next/navigation";

export default function MayoMypage() {
  const router = useRouter();
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
        <div className="flex text-8xl font-bold text text-[#F6F6F6] ">
          <div>
            <div
              className={cssCommen}
              onClick={() => router.push("/mayo/mypage/" + id + "/iteminfo")}
            >
              상품 정보
            </div>

            <div
              className={cssCommen}
              onClick={() => router.push("/mayo/mypage/" + id + "/storeinfo")}
            >
              가게 관리
            </div>
            <div
              className={cssCommen}
              onClick={() => router.push("/mayo/mypage/" + id + "/notice")}
            >
              마요 공지사항
            </div>
          </div>
          <div>
            <div
              className={cssCommen}
              onClick={() => router.push("/mayo/mypage/" + id + "/alarm")}
            >
              알림 설정
            </div>
            <div
              className={cssCommen}
              onClick={() => router.push("/mayo/mypage/" + id + "/service")}
            >
              고객 센터
            </div>
            <div
              className={cssCommen}
              onClick={() => router.push("/mayo/mypage/" + id + "/terms/" + id)}
            >
              약관 및 정책
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}
