(ns starthawk.client.util
    (:require
      [clojure.data.json :as json]
      [starthawk.client.env :refer [dev?]]))

(def write-str json/write-str)
(def read-str #(json/read-str % :key-fn keyword))

(defmacro defm
  [sym args & body]
  `(def ~sym
    (memoize (fn ~args ~@body))))

(defmacro defm-dev [sym & rest]
  `(def ~sym ((if dev? identity memoize) (fn ~@rest))))
