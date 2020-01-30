(ns todo-app.api-test
  (:require [clojure.test :refer :all]
            [todo-app.api :refer [api]]
            [ring.mock.request :as mock]))

(deftest InvalidRoute
  (testing "404 on invalid route"
    (let [response (api (mock/request :get "/"))]
      (is (= (:status response) 404)))))