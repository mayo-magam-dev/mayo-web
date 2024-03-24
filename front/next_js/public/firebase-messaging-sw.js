import { initializeApp } from "firebase/app";
import { getMessaging, getToken, onMessage } from "firebase/messaging";

const firebaseConfig = {
    apiKey: "AIzaSyAfFEA0BEEym6ULH_v-e-QlAY6rNNb1eDk",
    authDomain: "mayo-app-280d4.firebaseapp.com",
    projectId: "mayo-app-280d4",
    storageBucket: "mayo-app-280d4.appspot.com",
    messagingSenderId: "292349774898",
    appId: "1:292349774898:web:70c2131cf60d2057fbc02b",
    measurementId: "G-XWE31G7T43"
};

const app = initializeApp(firebaseConfig);
const messaging = getMessaging(app);

async function requestPermission() {
    console.log("권한 요청 중...");

    const permission = await Notification.requestPermission();
    if (permission === "denied") {
        console.log("알림 권한 허용 안됨");
        return;
    }

    console.log("알림 권한이 허용됨");

    const token = await getToken(messaging, {
        vapidKey: "BNxNy9jfEaF-09g8a8DFhKeXItf-_PLiJUEKVxd2PJOOWEedCxL-YFMBwEtN2Dxi-liI8kDPRVJRazh-eWaV7aU"
    });

    if (token) console.log("token: ", token);
    else console.log("Can not get Token");

    onMessage(messaging, (payload) => {
        console.log("메시지가 도착했습니다.", payload);
        // ...
    });
}

self.addEventListener("push", function (e) {
    if (!e.data.json()) return;

    const resultData = e.data.json().notification;
    const notificationTitle = resultData.title;
    const notificationOptions = {
        body: resultData.body,
        icon: resultData.image,
        tag: resultData.tag,
    };

    self.registration.showNotification(notificationTitle, notificationOptions);
});

// self.addEventListener("notificationclick", function (event) {
//     console.log("notification click");
//     const url = "/";
//     event.notification.close();
//     event.waitUntil(clients.openWindow(url));
// });

requestPermission();