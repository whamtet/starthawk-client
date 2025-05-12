(ns starthawk.client.web.views.starthawk
    (:require
      [hiccup2.core :as h]
      [starthawk.client.web.controllers.starthawk :as starthawk]))

(defn status [id]
  (starthawk/get-status id))
