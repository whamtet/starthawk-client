(ns starthawk.client.web.services.client
  (:require
   [clj-http.lite.client :as client]
   [clojure.java.io :as io]
   [starthawk.client.web.controllers.starthawk :as starthawk]
   [starthawk.client.util :as util]))

(defn- base [body]
  (->
   (client/post
    "https://api.starthawk.io/"
    {:headers
     {"Accept-Language" "en-US,en;q=0.9"
      "Connection" "keep-alive"
      "Cookie" "_gcl_au=1.1.514031732.1737365138; _ga=GA1.1.578396875.1737365142; _ga_DTLSWCN35C=GS1.1.1738159779.20.1.1738159780.0.0.0"
      "Origin" "https://www.starthawk.io"
      "Referer" "https://www.starthawk.io/"
      "Sec-Fetch-Dest" "empty"
      "Sec-Fetch-Mode" "cors"
      "Sec-Fetch-Site" "same-site"
      "User-Agent" "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36"
      "accept" "*/*"
      "authorization" (starthawk/authorization)
      "content-type" "application/json"}
     :body body})
     :body
     util/read-str))

(defn- body [src i page-size]
  (-> src io/resource slurp (format (* i page-size) (* i page-size))))
(defn- body-messages []
  (-> "messages" io/resource slurp))

(defn curl [src i page-size k]
  (-> (body src i page-size)
      base
      (get-in [:data k])))
(defn curl-messages
  "list of people we've messaged"
  []
  (-> (body-messages)
      base
      (get-in [:data :userMessages :conversations])))

(defn img->user [img]
  (-> (.split img "/") reverse second))
(defn conversation->users [{:keys [users]}]
  (map #(-> % :profilePic img->user) users))

(def messaged? #{} #_
  (->> (curl-messages) (mapcat conversation->users) set))

(defmacro defslurp [src page-size k]
  `(defn ~src [i#]
     (println ~(str src) i#)
     (curl ~(str src) i# ~page-size ~k)))

(defslurp cofounder 16 :usersLookingForCofounder)
;(defslurp premium 6 :premiumUsersLookingForCofounder)
;(defslurp vendor :usersLookingForVendor)

(defn spit-data [f s]
  (fn [i]
    (when-let [batch (not-empty (f i))]
      (->> batch
           pr-str
           (spit (format "data/%s/%04d.edn" s i)))
      true)))

#_
(->> (range)
     (map (spit-data cofounder "cofounder"))
     (take-while identity)
     dorun)
