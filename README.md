# CollegePA
Welcome to the College Planner Assistant app! This app is a Tasking app for college students. You will be able to add classes and     assignments. Essentially this is a digital planner

<h1>Installition</h1>
<br>
There is multiple ways to get this working. Installing the app_debug.apk with your emulator/mobile device. This app contains private keys so I'd prefer it to be done this way. Once you clone the repository you will see an .apk file  under the main directory for the app. Start your emulator that you would like to use (Recommended API 22 and up). In android studio you can run the command "adb install app-debug.apk" this will install the apk for you. Display all of your apps and look for the app called CollegePA.
<br>
Make sure that android's adb has been added to the PATH of your machine, whether that's Windows, Mac, or Linux.
<br>
Alternatively you can just run the app from android studio. Beware that doing this may result in google login not working.

<h1>How to Use</h1>
<br>
<ul>
	<li>
		When you first boot the application you will be at the login screen. Here you have 3 login types, FaceBook, Google, and email/password. A week after grades are turned in I will purge all accounts so don't worry about me keeping data for any of these authentication methods. If you want to sign up through email and password touch "First Time? Please Sign Up" and fill out the information
	</li>
	<li>
		After you login you will see a blank page with the title <b>Courses</b>. From here you can hit the 3 dots icon at the top right and select add course. Fill out the information and you will see that a course has been added. The emphasis on this project is cloud storage so I recommend you test on multiple emulators. You can also refresh the page by swiping down from top to bottom (If you make changes live from another emulator you may need to swipe). Once you add the course you can touch it. This will bring you to a new activity. This activity is not populated yet but from the options icon at top right you can change the courses color and or delete the course. With the button at the top left you can navigate to different places. Lets start with adding an assignment 
	</li>
	<li>
		Select assignments in navigation menu. There is not much fuctionality here but you can add an assignment for a specific course. At the top right click the button and add an assignment. Once you finish navigate back to assignment and you will see the assignment in that menu.
	</li>
	<li>
		Grades has no fuctionality yet
	</li>
	<li>
		Account displays your email address. Canvas button is at bottom but has no fuctionality
	</li>
	<li>About has information about the app</li>
	<li>Settings has no fuctionality</li>
	<li>Contact us allows user to send bugs and comments through email. Assuming your emulator has an email app</li>
	<li>Logout logs you out of the application</li>
</ul>



<h1>Goal of this Project</h1>
<br>
The goal of this project is to test out google's firebase api and to store data in the cloud rather than on the device itself (App will save data offline to!). I wanted to make a simple app that can keep track of your assignments with (Mainly for classes that don't use canvas, I have a few of them).
<h1>Ideas that didn't make it into project</h1>
<ul>
	<li>
	Using the app alongside canvas with canvas API. Student logs in with canvas and their classes are populated automatically
	</li>
	<li>
	Use the drive api so user can store notes, photos with their classes
	</li>
</ul>

<h1>Screenshots</h1>
<br>
![alt tag](https://github.com/FMurry/CollegePA/blob/master/Screenshots/AppIcon.png)
<br>
Login Page:
<br>
![alt tag](https://github.com/FMurry/CollegePA/blob/master/Screenshots/login.png)
<br>
Course Main Page:
<br>
![alt tag](https://github.com/FMurry/CollegePA/blob/master/Screenshots/Course.png)

<br>
Maintained by Frederic Murry and Erick Vazquez

