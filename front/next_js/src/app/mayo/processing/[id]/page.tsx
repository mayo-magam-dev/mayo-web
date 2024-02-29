"use client";
// http://localhost:3000/mayo/processing/Asb0QBuQ9gmEyyWlSVhY
import MayoHeader from "@/atoms/molecule/mayoHeader";
import NavigationLeft from "@/atoms/molecule/navigationLeft";
import NewOrder from "@/atoms/molecule/newOrder";

export default function MayoProcessing() {
  return (
    <main className="rounded-[3rem] w-[95%] h-[105rem] bg-blue m-[5rem]">
      <header>
        <MayoHeader />
      </header>
      <div className="flex ">
        <NavigationLeft />
        <div>
          <div className="text-white text-6xl font-bold"></div>
          <div className="text-white text-6xl font-bold">2</div>
          <div></div>
        </div>
        <div>2</div>
      </div>
    </main>
  );
}
