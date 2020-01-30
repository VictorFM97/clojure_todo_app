(ns todo-app.profile.business-test
  (:require [clojure.test :refer :all]
            [todo-app.profile.business :as b]))

(def example-profiles [{:id 1 :name "Andy"}
                       {:id 2 :name "Jhonny"}
                       {:id 3 :name "Carl"}])

(deftest adding
  (testing "Shouldn't add invalid profile"
    (is (= (b/add! {}) "Invalid profile"))
    (is (= (b/add! {:name 1}) "Invalid profile"))
    (is (= (b/add! {:name ""}) "Invalid profile"))))

(deftest get-specific
  (testing "should retrieve specific profile"
    (with-redefs [b/filter-profiles (fn [pred]
                                      (filter pred example-profiles))]
      (let [profile (b/get-by-id 1)]
        (is (and (= (:name profile) "Andy")
                 (= (:id profile) 1))))
      (let [profile (b/get-by-id 2)]
        (is (and (= (:name profile) "Jhonny")
                 (= (:id profile) 2))))
      (let [profile (b/get-by-id 3)]
        (is (and (= (:name profile) "Carl")
                 (= (:id profile) 3)))))))