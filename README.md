## Document back button behavior
When I go from any screen, like Tasks or Calendar, to another screen, pressing the back button always takes me back to Notes. If I'm already on Notes and press back, the app closes. This happens because I set popUpTo(Routes.Notes.route) so the back stack always returns to Notes. Using launchSingleTop and restoreState = true makes sure each screen keeps its content and doesn’t get added multiple times to the stack.

## AI 
I used AI to help me understand restoreState and how it works, which ensures that each screen keeps its content when navigating. I also used AI to help debug an issue on the Tasks screen where the checkbox didn’t appear checked immediately after clicking. After adjusting the Task data class, the checkboxes now update correctly in real time. AI did not misunderstand the navigation behavior.
