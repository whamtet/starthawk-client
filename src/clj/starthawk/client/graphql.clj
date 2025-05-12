(ns starthawk.client.graphql
    (:require
      [clojure.java.io :as io]
      [starthawk.client.util :as util]))

(util/defm-dev slurp-resource [f]
  (->> f (str "graphql/") io/resource slurp))

(defn login-body []
  (format (slurp-resource "login")
          (System/getenv "STARTHAWK_USER")
          (System/getenv "STARTHAWK_PASS")))

(defn status-body [user]
  (format (slurp-resource "status") user))
