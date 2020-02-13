(ns todo-app.api-test
  (:require [midje.sweet :refer [facts fact => against-background after]]
            [clojure.data.json :as json]
            [ring.mock.request :as mock]
            [todo-app.api :refer [api]]
            [todo-app.profile.repository :as profile]
            [todo-app.tasks.repository :as tasks]))

(defn json-to-map
  [body]
  (json/read-str body :key-fn keyword))

(defn mock-request
  [method uri body]
  (api (->
        (mock/request method uri)
        (mock/json-body body))))

(facts "Invalid routes in the API should return 404 status"
       (fact "404 on invalid route"
             (let [response (api (mock/request :get "/"))]
               (:status response) => 404)))

(facts "Profile routes"
       (fact "sending invalid profile"
             (let [response (mock-request :post "/api/profile/add" {:name ""})]
               (:status response) => 422
               (:body response) => "Invalid profile")
             (let [response (mock-request :post "/api/profile/add" {})]
               (:status response) => 422
               (:body response) => "Invalid profile"))

       (fact "sending valid profile"
             (let [response (mock-request :post "/api/profile/add" {:name "Victor"})
                   body (json-to-map (:body response))]
               (:status response) => 200
               (and (= (:id body) 1)
                    (= (:name body) "Victor")) => true)
             (let [response (mock-request :post "/api/profile/add" {:name "Carol"})
                   body (json-to-map (:body response))]
               (:status response) => 200
               (and (= (:id body) 2)
                    (= (:name body) "Carol")) => true))

       (fact "fetching profiles"
             (let [response (mock-request :get "/api/profile/1/" {})
                   body (json-to-map (:body response))]
               (:status response) => 200
               (and (= (:name body) "Victor")
                    (= (:id body) 1)) => true)
             (let [response (mock-request :get "/api/profile/2/" {})
                   body (json-to-map (:body response))]
               (:status response) => 200
               (and (= (:name body) "Carol")
                    (= (:id body) 2)) => true))
       (profile/clear!))

(facts "tasks"
       (fact "sending invalid task"
             (let [response (mock-request :post "/api/task/add" {:profile-id 0})]
               (:status response) => 422
               (:body response) => "Invalid task")
             (let [response (mock-request :post "/api/task/add" {:profile-id 1 :title ""})]
               (:status response) => 422
               (:body response) => "Invalid task")
             (let [response (mock-request :post "/api/task/add" {:profile-id 1 :title "abc" :description ""})]
               (:status response) => 422
               (:body response) => "Invalid task")
             (let [response (mock-request :post "/api/task/add" {})]
               (:status response) => 422
               (:body response) => "Invalid task"))

       (fact "sending valid task"
             (let [response (mock-request :post "/api/task/add" {:profile-id 1 :title "abc" :description "def"})
                   body (json-to-map (:body response))]
               (:status response) => 200
               (and (= (:id body) 1)
                    (= (:profile-id body) 1)
                    (= (:title body) "abc")
                    (= (:description body) "def")
                    (false? (:deleted body))
                    (false? (:done body))) => true)
             (let [response (mock-request :post "/api/task/add" {:profile-id 1 :title "123" :description "456"})
                   body (json-to-map (:body response))]
               (:status response) => 200
               (and (= (:id body) 2)
                    (= (:profile-id body) 1)
                    (= (:title body) "123")
                    (= (:description body) "456")
                    (false? (:deleted body))
                    (false? (:done body))) => true)
             (let [response (mock-request :post "/api/task/add" {:profile-id 2 :title "123" :description "456"})
                   body (json-to-map (:body response))]
               (:status response) => 200
               (and (= (:id body) 3)
                    (= (:profile-id body) 2)
                    (= (:title body) "123")
                    (= (:description body) "456")
                    (false? (:deleted body))
                    (false? (:done body))) => true))

       (fact "deleting task that exists"
             (let [response (mock-request :delete "/api/task/delete" {:id 1})
                   body (json-to-map (:body response))]
               (:status response) => 200
               (and (= (:id body) 1)
                    (true? (:deleted body))) => true)
             (let [response (mock-request :delete "/api/task/delete" {:id 2})
                   body (json-to-map (:body response))]
               (:status response) => 200
               (and (= (:id body) 2)
                    (true? (:deleted body))) => true))

       (fact "deleting task that doesn't exists"
             (let [response (mock-request :delete "/api/task/delete" {:id 999})]
               (:status response) => 404
               (:body response) => "Task not found"))

       (fact "marking task that exists as done/undone"
             (let [response (mock-request :put "/api/task/done" {:id 2 :done true})
                   body (json-to-map (:body response))]
               (:status response) => 200
               (and (= (:id body) 2)
                    (true? (:done body))) => true)
             (let [response (mock-request :put "/api/task/done" {:id 2 :done false})
                   body (json-to-map (:body response))]
               (:status response) => 200
               (and (= (:id body) 2)
                    (false? (:done body))) => true))

       (fact "marking task that doesn't exists as done/undone"
             (let [response (mock-request :put "/api/task/done" {:id 999})]
               (:status response) => 404
               (:body response) => "Task not found"))

       (facts "getters"
              (fact "getting last task added of profile"
                    (let [response (mock-request :get "/api/task/1/most-recent" {})
                          body (json-to-map (:body response))]
                      (:status response) => 200
                      (and (= (:id body) 2)
                           (= (:profile-id body) 1)
                           (= (:title body) "123")
                           (= (:description body) "456")) => true)
                    (let [response (mock-request :get "/api/task/2/most-recent" {})
                          body (json-to-map (:body response))]
                      (:status response) => 200
                      (and (= (:id body) 3)
                           (= (:profile-id body) 2)
                           (= (:title body) "123")
                           (= (:description body) "456")) => true))
              (fact "getting all tasks of profile"
                    (let [response (mock-request :get "/api/task/1/all" {})
                          body (json-to-map (:body response))]
                      (:status response) => 200
                      (count body) => 2)
                    (let [response (mock-request :get "/api/task/2/all" {})
                          body (json-to-map (:body response))]
                      (:status response) => 200
                      (count body) => 1))
              (fact "getting specific task"); TODO: Create tests
              )
       (tasks/clear!))