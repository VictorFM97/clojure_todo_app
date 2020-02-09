(ns todo-app.profile.business-test
  (:require [midje.sweet :refer :all]
            [todo-app.profile.business :as b]))

(def example-profiles [{:id 1 :name "Andy"}
                       {:id 2 :name "Jhonny"}
                       {:id 3 :name "Carl"}])

(fact "Shouldn't add invalid profile"
      (b/add! {}) => false
      (b/add! {:name 1}) => false
      (b/add! {:name ""}) => false)

(fact "should retrieve specific profile"
      (with-redefs [b/filter-profiles (fn [pred]
                                        (filter pred example-profiles))]
        (let [profile (b/get-by-id 1)]
          (and (= (:name profile) "Andy")
               (= (:id profile) 1)) => true)
        (let [profile (b/get-by-id 2)]
          (and (= (:name profile) "Jhonny")
               (= (:id profile) 2)) => true)
        (let [profile (b/get-by-id 3)]
          (and (= (:name profile) "Carl")
               (= (:id profile) 3)) => true)))