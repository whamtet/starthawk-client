(ns starthawk.client.web.routes.api
  (:require
    [starthawk.client.web.controllers.health :as health]
    [starthawk.client.web.controllers.starthawk :as starthawk]
    [starthawk.client.web.middleware.exception :as exception]
    [starthawk.client.web.middleware.formats :as formats]
    [integrant.core :as ig]
    [reitit.coercion.malli :as malli]
    [reitit.ring.coercion :as coercion]
    [reitit.ring.middleware.muuntaja :as muuntaja]
    [reitit.ring.middleware.parameters :as parameters]
    [reitit.swagger :as swagger]))

(def route-data
  {:coercion   malli/coercion
   :muuntaja   formats/instance
   :swagger    {:id ::api}
   :middleware [;; query-params & form-params
                parameters/parameters-middleware
                  ;; content-negotiation
                muuntaja/format-negotiate-middleware
                  ;; encoding response body
                muuntaja/format-response-middleware
                  ;; exception handling
                coercion/coerce-exceptions-middleware
                  ;; decoding request body
                muuntaja/format-request-middleware
                  ;; coercing response bodys
                coercion/coerce-response-middleware
                  ;; coercing request parameters
                coercion/coerce-request-middleware
                  ;; exception handling
                exception/wrap-exception]})

;; Routes
(defn api-routes [_opts]
  [["/swagger.json"
    {:get {:no-doc  true
           :swagger {:info {:title "starthawk.client API"}}
           :handler (swagger/create-swagger-handler)}}]
   ["/login"
    (fn [req]
      {:status 200
       :headers {"Content-Type" "text/plain"}
       :body (starthawk/refresh-session!)})]
   ["/health"
    ;; note that use of the var is necessary
    ;; for reitit to reload routes without
    ;; restarting the system
    {:get #'health/healthcheck!}]])

(derive :reitit.routes/api :reitit/routes)

(defmethod ig/init-key :reitit.routes/api
  [_ opts]
  ["" route-data (api-routes opts)])
