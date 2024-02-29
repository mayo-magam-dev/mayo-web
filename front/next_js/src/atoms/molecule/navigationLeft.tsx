"use client";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

export default function NavigationLeft() {
  const router = useRouter();

  return (
    <div className="h-[90rem] w-[20rem] text-white text-6xl font-bold bg-blue">
      <div className="flex justify-center items-center h-[18%] bg-red">
        처리중
      </div>
      <div className="flex justify-center items-center h-[18%] bg-black">
        완료
      </div>
      <div className="flex justify-center items-center h-[18%] bg-red">
        등록
      </div>
      <div className="flex justify-center items-center h-[18%] bg-black">
        마이페이지
      </div>
      <div className="flex justify-center items-center h-[18%] bg-red">
        설정
      </div>
      <div className="h-[10%]">사진</div>
    </div>
  );
}
