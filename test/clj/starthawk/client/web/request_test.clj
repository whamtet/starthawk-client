(ns starthawk.client.web.request-test
  (:require  [clojure.test :refer [deftest testing is use-fixtures]]
             [starthawk.client.test-utils :refer [system-state system-fixture GET]]))

(use-fixtures :once (system-fixture))

(deftest health-request-test []
  (testing "happy path"
    (let [handler (:handler/ring (system-state))
          params {}
          headers {}
          response (GET handler "/api/health" params headers)]
      (is (= 200 (:status response))))))
