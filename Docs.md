# Documentation

We designed the user communities based on the next xxx steps.
First, we analyze all the data from the users' review and get the metadata(score, helpful, helpless, sentiments). 
Then we analyze those data on the user perspective by summarizing all reviews posted by the user. Then we can come up with the average score, the number of reviews, thumbs up from other users, thumbs down from other users, and the thumbs up percentage for each user.
To determine if the user likes or dislikes those movies that he/she has already given a review is simple, we just need the find the score that he/she rated on the review and combine it with the sentiment of the review content.

## fetch/{user}/{page}

We have used the user based nearest-neighbour collaborative filtering to come up with the second part of the page. Since all the reviews from each user contain the score that he/she gives to the movie, then we used them to simply generate an N*P matrix. N represents the number of users in the same community of the given user. P represents the total number of pages. Therefore, we simply find all the similar users and predict whether the given user likes the other pages or not. Then we pick the page with the highest possibility that the given user likes it.

## Suggest algorithm

The social graph is a kind of connection between different users. According to Schall(2015), social network prediction is really important to the new user to find new contacts in and existing network. Those connections are normally positive connections such as friends, family. So those people who are got connected usually have the same interests and hobbies. Therefore we can come up with the suggesting algorithm based on the social graph. We have a new user $u_1$ and the social graph $G<U, E>$. Then once the new user $u_1$ decides to add some connection with someone he/she knew, then we can give some suggestions based on the news connection. For example, if the new user $u_1$ adds someone who is watching basketball every day. Then we can determine that the new user $u_1$ probably likes basketball. If the new user $u_1$ connects more and more people, we can summarize the interests of those people that the user connected to come up with a list of interest possibilities that from the most of his/her connection likes to the least of his/her connection likes. 


## Reference
Schall, D. (2015). Social Network-Based Recommender Systems. Springer International Publishing.


