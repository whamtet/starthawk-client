(ns starthawk.client.web.views.starthawk
    (:require
      [hiccup2.core :as h]
      [starthawk.client.web.controllers.starthawk :as starthawk]))

(def html #(-> % h/html str))

(defn plain-response [body]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body body})

(defn retarget-response [id body]
  {:status 200
   :headers {"Content-Type" "text/html"
             "HX-Reswap" "outerHTML"
             "HX-Retarget" (str "#" id)}
   :body (html body)})

(defn status [{:keys [params path-params]}]
  (let [{:keys [id]} path-params
        {:keys [profilePic]} params
        status (starthawk/get-status id)]
    (if (= "Not very active" status)
      (retarget-response
       id
       [:div {:style "padding: 4px; margin-bottom: 4px"
              :id id}
        [:div {:style "margin-bottom: 4px, display: flex"}
         [:a {:href (format "https://www.starthawk.io/profile/%s" id)
              :target "_blank"}
          [:img {:style {:width "64px" :margin-right "1rem"}
                 :src profilePic}]]]
        [:hr]])
      (plain-response status))))
