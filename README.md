![Logo](./docs/logo.png)

Final Project of Afeka's `Mobile Apps - 25A10357` Class.

## How to Run

> To use this app (with it's full feature set), you'll need a alphavantage api.
> If you don't have one, you can still run the app without it. just create a fake file instead.

- Save your `token` in the project root directory under `local.env`:

  ```properties
  STOCK_API_KEY=<Your Token>
  ```

## Table of Contents

- [Overview](#overview)
- [App Flow](#app-flow)
  - [Home Screen (Profile)](#1-home-screen-profile)
  - [Leaderboard](#3-leaderboard)
  - [Explore Stocks](#4-explore-stocks)
    - [Stock Details](#4-stock-details)
    - [Buy/Sell](#5-buysell)
- [Features who didn't make the cut (Planned in the Features)](#features-who-didnt-make-the-cut-planned-in-the-features)
- [Technical Details](#technical-details)


# Overview

Tardely is a stock market simulator built using `Kotlin 2.1.0`, designed for Android devices.

## App Flow

[//]: # (![Overview]&#40;./docs/app_overview.png&#41;)

### 1. Home Screen (Profile)


![Home](./docs/home.png)

- **Profile Card** - displays the profile info and lets you edit your own profile 
- **Watchlist & Owned Stocks** - Uses a viewpager to display the user's `watchlist` and `owned stocks` updates using `ProfileViewModel`

### 3. Leaderboard

![Leaderboard](./docs/leaderboard.png)

- Creates the leaderboard using `RecyclerView` and `LeaderboardAdapter` to display the top players
- Updates the leaderboard using `LeaderboardViewModel` and `Profile Manager` with read-time data from `Firebase Firestore` with the Observer pattern.
- Clicking on a user will navigate to their profile.
- **follow** - follows the profile to let you see their updates. (A follow tab maybe in the future)

### 4. Explore Stocks
| ![Explore Stocks](./docs/explore_stocks.png) | ![Deep Search](./docs/deep_search.png) |
| ---------------------------------------------- |----------------------------------------|

- Creates the stock list using `RecyclerView` and `StockAdapter` to display the stocks that are currently available to simulate.
- Updates the stock list using `StockViewModel` and `StockManager` with read-time data from `Firebase Realtime` using the Observer pattern.
- Clicking on a stock will navigate to the stock details.
- **Search** - Uses `SearchView` to filter the stocks by name.
- **Deep Search** - When no stock is found in the search, the app will try to find the stock in the `AlphaVantage API Key` and add it to the list. (Photo to the right).
- **Watchlist** - Adds the stock to the profile's watchlist using `ProfileManager`.

### 4. Stock Details

![Stock Details](./docs/stock_details.png)

- Uses `StockViewModel` to display the details of the chosen stock.
- **Buy/Sell** - Navigate to buy/sell fragment.
- **Add to Watchlist** - Uses `ProfileManger` to add/remove stocks from the watchlist.
- **Interactive Chart** - Uses `MPAndroidChart` to display the stock's price history in the form of a `candlestick chart`.


### 5. Buy/Sell
| ![Buy](./docs/buy.png) | ![Sell](./docs/sell.png) |
|------------------------|--------------------------|

- Uses `StockViewModel` to display the details of the chosen stock.
- **Buy/Sell** - Uses `ProfileManager` to buy/sell stocks.
- **stop loss** - (Coming Soon) Uses `ProfileManager` to set a stop loss for the stock.

# Features who didn't make the cut (Planned in the Features)
- **Stop Loss** - A feature that lets you set a stop loss for a stock.
- **Follow Feed** - A feature that lets you see the updates of the profiles you follow.
- **Market Education** - A Page that lets you learn about the stock market and terms.
- **Realtime-Market Data** - A service that update the RealtimeDB with the current changes in the stock market.

# Technical Details
- **Data Parsing** - Using price data from Alphavantage API. the `StockParser` serializes the dates into floats and puts them `CandleEntry` objects. 
- **Deserializing Dates** - The `DateAxisFormater` and `DateTimeAxisFormater` gets the floats and converts them back to dates, to be displayed in the chart.
- **Profile Manager** - Manages the user's profile data and updates it in and from the `Firebase Firestore`.
- **Stock Manager** - Manages the stock data and updates it in and from the `Firebase RealtimeDB`.
- **StockViewModel** - Used to select the current stock and transfer updates and data on it.
- **ProfileViewModel** - Used to select the current profile and transfer updates and data on it.

## Authors

- [@lordYorden](https://github.com/lordYorden)
