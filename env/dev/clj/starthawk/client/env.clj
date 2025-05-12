(ns starthawk.client.env
  (:require
    [clojure.tools.logging :as log]
    [starthawk.client.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[client starting using the development or test profile]=-"))
   :start      (fn []
                 (log/info "\n-=[client started successfully using the development or test profile]=-"))
   :stop       (fn []
                 (log/info "\n-=[client has shut down successfully]=-"))
   :middleware wrap-dev
   :opts       {:profile       :dev}})

(def dev? true)
(def prod? false)
