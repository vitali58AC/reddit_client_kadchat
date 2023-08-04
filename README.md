Kadchat

Used by: Kotlin Flow, Room, Retrofit, Moshi, Glide, Kotlin Coroutine, Navigation component,
Reddit API, ViewPager2, Koin, AppAuth, Paging 3.

Created a client for reddit, here are some of the features of this application:

After completing the tutorial on the Intro screens, the user needs to log into their
Reddit account (using OAuth2). After a successful login, we get to the main screen,
bottom navigation view allows you to select three tabs.

    - The first tab allows you to view the paginated list of subreddits, we can subscribe,
    add the subreddit to favorites (create a local copy of the data in the database),
    or go to the paginated list of posts in this subreddit. Posts can also be added to favorites,
    you can also go to the comments under any post. By clicking on the author of the post,
    you can go to the profile of the user who wrote this post.

    - The second tab takes the data from the database that we saved to favorites.

    - The third tab is the profile of the current user, here we can view the list of friends,
    clear the local storage or log out of the Reddit account.

All images are loaded using the Glide library. All dependencies are injected using the Koin library.
The MVVM architecture is used. All transitions between the screen are accompanied by animation,
loading data using Flow displays the preloader.

To use the application, you will need a CLIENT_ID,
which can be obtained from reddit - https://www.reddit.com/prefs/apps.