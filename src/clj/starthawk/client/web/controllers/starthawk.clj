(ns starthawk.client.web.controllers.starthawk
    (:require
      [clj-http.lite.client :as client]
      [starthawk.client.graphql :as graphql]
      [starthawk.client.util :as util]))

(def session (atom nil))

(defn- get-session []
  (->
   (client/post "https://api.starthawk.io/"
                {:headers {"content-type" "application/json"}
                 :body (graphql/login-body)})
   :body
   util/read-str
   :data
   :signIn
   :token))

(defn refresh-session! []
  (reset! session (get-session)))

(defn- authorization []
  (str "Bearer " (or @session (refresh-session!))))

(defn get-status [id]
  (->
   (client/post "https://api.starthawk.io/"
                {:headers {"content-type" "application/json"
                           "authorization" (authorization)}
                 :body (graphql/status-body id)})
   :body
   util/read-str
   :data
   :user
   :activityStatus))
