(ns todo-app.api-test
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]
            [todo-app.api :refer [api]]
            [ring.mock.request :as mock]
            [todo-app.profile.repository :as profile]
            [todo-app.tasks.repository :as tasks]))

(defn json-to-map
  [body]
  (json/read-str body :key-fn keyword))

(deftest invalid-route
  (testing "404 on invalid route"
    (let [response (api (mock/request :get "/"))]
      (is (= (:status response) 404)))))

(defn mock-request
  [method uri body]
  (api (->
        (mock/request method uri)
        (mock/json-body body))))

(deftest profile
  (testing "sending invalid profile"
    (let [response (mock-request :post "/api/profile/add" {:name ""})]
      (is (= (:body response) "Invalid profile")))
    (let [response (mock-request :post "/api/profile/add" {})]
      (is (= (:body response) "Invalid profile"))))
  (testing "sending valid profile"
    (let [response (mock-request :post "/api/profile/add" {:name "Victor"})
          body (json-to-map (:body response))]
      (is (= (:status response) 200))
      (is (and (= (:id body) 1)
               (= (:name body) "Victor"))))
    (let [response (mock-request :post "/api/profile/add" {:name "Carol"})
          body (json-to-map (:body response))]
      (is (= (:status response) 200))
      (is (and (= (:id body) 2)
               (= (:name body) "Carol")))))
  (testing "fetching profiles"
    (let [response (mock-request :get "/api/profile/1/" {})
          body (json-to-map (:body response))]
      (is (= (:status response) 200))
      (is (and (= (:name body) "Victor")
               (= (:id body) 1))))
    (let [response (mock-request :get "/api/profile/2/" {})
          body (json-to-map (:body response))]
      (is (= (:status response) 200))
      (is (and (= (:name body) "Carol")
               (= (:id body) 2)))))
  (profile/clear!))

(deftest tasks
  (testing "sending invalid task"
    (let [response (mock-request :post "/api/task/add" {:profile-id 0})]
      (is (= (:body response) "Invalid task")))
    (let [response (mock-request :post "/api/task/add" {:profile-id 1 :title ""})]
      (is (= (:body response) "Invalid task")))
    (let [response (mock-request :post "/api/task/add" {:profile-id 1 :title "abc" :description ""})]
      (is (= (:body response) "Invalid task")))
    (let [response (mock-request :post "/api/task/add" {})]
      (is (= (:body response) "Invalid task"))))
  (testing "sending valid task"
    (let [response (mock-request :post "/api/task/add" {:profile-id 1 :title "abc" :description "def"})
          body (json-to-map (:body response))]
      (is (= (:status response) 200))
      (is (and (= (:id body) 1)
               (= (:profile-id body) 1)
               (= (:title body) "abc")
               (= (:description body) "def")
               (false? (:deleted body))
               (false? (:done body)))))
    (let [response (mock-request :post "/api/task/add" {:profile-id 1 :title "123" :description "456"})
          body (json-to-map (:body response))]
      (is (= (:status response) 200))
      (is (and (= (:id body) 2)
               (= (:profile-id body) 1)
               (= (:title body) "123")
               (= (:description body) "456")
               (false? (:deleted body))
               (false? (:done body)))))
    (let [response (mock-request :post "/api/task/add" {:profile-id 2 :title "123" :description "456"})
          body (json-to-map (:body response))]
      (is (= (:status response) 200))
      (is (and (= (:id body) 3)
               (= (:profile-id body) 2)
               (= (:title body) "123")
               (= (:description body) "456")
               (false? (:deleted body))
               (false? (:done body))))))
  (testing "deleting task")
  ; TODO: Create tests
  (testing "marking task as done/undone")
  ; TODO: Create tests
  (testing "getters"
    (testing "getting last task added of profile"
      (let [response (mock-request :get "/api/task/1/most-recent" {})
            body (json-to-map (:body response))]
        (is (= (:status response) 200))
        (is (and (= (:id body) 2)
                 (= (:profile-id body) 1)
                 (= (:title body) "123")
                 (= (:description body) "456"))))
      (let [response (mock-request :get "/api/task/2/most-recent" {})
            body (json-to-map (:body response))]
        (is (= (:status response) 200))
        (is (and (= (:id body) 3)
                 (= (:profile-id body) 2)
                 (= (:title body) "123")
                 (= (:description body) "456")))))
    (testing "getting all tasks of profile"
      (let [response (mock-request :get "/api/task/1/all" {})
            body (json-to-map (:body response))]
        (is (= (:status response) 200))
        (is (= (count body) 2)))
      (let [response (mock-request :get "/api/task/2/all" {})
            body (json-to-map (:body response))]
        (is (= (:status response) 200))
        (is (= (count body) 1))))
    (testing "getting specific task"))
    ; TODO: Create tests
  (tasks/clear!))