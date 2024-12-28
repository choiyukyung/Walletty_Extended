import { useEffect, useState } from "react";
import MyHeader from "../components/MyHeader";
import { useNavigate } from "react-router-dom";
import RankingTopList from "../components/RankingTopList";
import RankingFriendList from "../components/RankingFriendList";

const Ranking = () => {
  const navigate = useNavigate();
  const [rankTopList, setRankTopList] = useState([]);
  const [rankFriendList, setRankFriendList] = useState([]);

  useEffect(() => {
    if (localStorage.getItem("isLoggedIn") === "true") {
      fetch("/ranking/top3", {
        method: "GET",
        body: JSON.stringify({}),
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((result) => result.json())
        .then((result) => {
          setRankTopList(result);
        })
        .catch((err) => {
          console.log(err);
        });
      fetch("/ranking/friend", {
        method: "GET",
        body: JSON.stringify({}),
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((result) => result.json())
        .then((result) => {
          setRankFriendList(result);
        })
        .catch((err) => {
          console.log(err);
        });
    } else {
      navigate("/login");
    }
    console.log("new render");
  }, []);

  useEffect(() => {}, [rankTopList, rankFriendList]);

  return (
    <div>
      <MyHeader nowpage={"ranking"} />
      {rankTopList[0] && <RankingTopList rankingTopList={rankTopList} />}
      {rankFriendList[0] && (
        <RankingFriendList rankingFriendList={rankFriendList} />
      )}
    </div>
  );
};

export default Ranking;
