(ns starthawk.client.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[client starting]=-"))
   :start      (fn []
                 (log/info "\n-=[client started successfully]=-"))
   :stop       (fn []
                 (log/info "\n-=[client has shut down successfully]=-"))
   :middleware (fn [handler _] handler)
   :opts       {:profile :prod}})
