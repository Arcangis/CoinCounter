## NOT FINISHED YET!

## Introduction

Coin Counter is an easy-to-use Android application that can count how much money there is in coins, using the smartphone camera. It also provides a currency-convert calculator.

## Inspiration

This application was designed to make the tedious task of counting coins quicker and less tiring, especially for small commerce, who always receive a lot of them from their clients.  It also intends to help small business owners and costumers not to touch in coins as a measure of preventing the spread of COVID-19.

## What it does

It uses the smartphone camera to identify the value of each coin in an image and displays the sum of them. It also has a currency-convert calculator, where you can fill an amount and an exchange rate, and it returns the conversion result.

## How we built it

Using the Android Studio API, as well as Pytorch and OpenCV.

## How to use this application

Once you access the app, you can choose to use the counter or the currency converter.
If you choose the counter, you must point your smartphone's camera at the coins, which must be on a flat, opaque surface, and then you can view the count value.
If you choose the converter, you must inform the quantity and the exchange rate, and then you can view the converted value.  

## Challenges we ran into

We have almost no experience in programming in java and XML, and it's the first time we made an application that uses the android camera, pytorch_android and OpenCV for Android.

## What we learned

We learned significant aspects of the Java language, as well as how to perform image morphology with OpenCV for Android and how to translate a PyTorch model to Android.

## What's next for My Coin Counter

For now, this application only supports the Brazilian currency ("R$"), but it can be improved to support other currencies as well. Also, it will be useful to get the exchange rate between coins from the internet rather than ask the user to fill it.
