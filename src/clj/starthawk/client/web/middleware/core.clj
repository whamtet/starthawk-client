(ns starthawk.client.web.middleware.core
  (:require
    [starthawk.client.env :as env]
    [ring.middleware.basic-authentication
     :refer [wrap-basic-authentication]]
    [ring.middleware.defaults :as defaults]
    [ring.middleware.session.cookie :as cookie]))

(defn authenticated? [name pass]
  (and (= name (System/getenv "STARTHAWK_USER"))
       (= pass (System/getenv "STARTHAWK_PASS"))))

(defn wrap-base
  [{:keys [metrics site-defaults-config cookie-secret] :as opts}]
  (let [cookie-store (cookie/cookie-store {:key (.getBytes ^String cookie-secret)})]
    (fn [handler]
      (-> ((:middleware env/defaults) handler opts)
          (defaults/wrap-defaults
           (assoc-in site-defaults-config [:session :store] cookie-store))
          (wrap-basic-authentication authenticated?)
          ))))
