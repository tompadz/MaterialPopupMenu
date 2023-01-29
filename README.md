# Material popup menu 

My vision of popup menu design from material.io <br>

[![](https://jitpack.io/v/tompadz/MaterialPopupMenu.svg)](https://jitpack.io/#tompadz/MaterialPopupMenu)

- The menu should be able to add a title;
- Each menu list item should be highlighted with a Material Divider;
- Each item in the menu list must have a title and an icon;

|![screenshot](https://github.com/tompadz/MaterialPopupMenu/blob/master/img/preview2.jpeg?raw=true)|![screenshot](https://github.com/tompadz/MaterialPopupMenu/blob/master/img/preview2.gif?raw=true)|
|--|--|

## Installation

Add it in your root build.gradle at the end of repositories:

```gradle	
	allprojects {
	   repositories {
		...
		maven { url 'https://jitpack.io' }
	    }
	}
```

Add the dependency

```gradle
	dependencies {
	    implementation 'com.github.tompadz:MaterialPopupMenu:1.0.2'
	}
```

## Usage

Create xml menu file

```xml
    <menu>
        <item
            android:icon="@drawable/ic_settings"
            android:title="Position" />
        <item
            android:icon="@drawable/ic_edit"
            android:title="Edit" />
        <item
            android:icon="@drawable/ic_delete"
            android:title="Delete" />
        <item
            android:icon="@drawable/ic_search"
            android:title="Search" />
    </menu>
```

Create and show popup menu

```kotlin
    private fun showMenu(view: View) {
        val menu = MaterialPopupMenu(
            context = this,
            view = view,
        )
        menu.addMenuAndShow(R.menu.menu)
    }
```

## Customization

To customize the menu, you need to create a configuration and pass it to the menu constructor

```kotlin
    val config = MaterialPopupConfiguration.Builder()
            .setTitle("Actions")
            .blurEnable(true)
            .build()

    val menu = MaterialPopupMenu(
            context = this,
            view = view,
            config = config,
    )
```

### Configuration params

| Code                      | Description                                                                                                                                |
|---------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| .setTitle(string?)        | Sets the title for the menu. Title will be hidden if its value is `null` <br> Default `null`                                               |
| .blurEnable(boolean)      | Turns on and off the use of blur when setting the background color. <br> Default `true`                                                    |
| .setBackgroundColor(Int?) | Sets the background color value, if `null`, takes the `surface` color value from the theme <br> Default `null`                             |
| .setItemTextColor(Int?)   | Sets the color value for the list items, if the value is `null` sets the `onSurface` color from the application theme <br> Default `null`  |
| .setBlurRadius(float)     | Sets the background blur radius value if `.blurEnable(true)` <br> Default `50f`                                                            |
| .setBehindDimAmong(float) | Sets the value for dimming the background when a menu appears. <br> Default `0.3f`                                                         |


### Menu display options

There are many ways to show a menu, the easiest one is to call the `menu.addMenuAndShow(R.menu.menu)` method <br>
It is not necessary to call the menu immediately after creation, it is possible to separate these functions

```kotlin
    val menu = MaterialPopupMenu(
            context = this,
            view = view,
    )
    menu.addMenu(R.menu.menu)
    ....
    menu.show()
```

Since the menu is a `PopupWindow`, you have the ability to call the menu in any possible method from the api

```kotlin
    val menu = MaterialPopupMenu(
            context = this,
            view = view,
    )
    menu.addMenu(R.menu.menu)
    ....
    menu.showAsDropDown(view)
```

## Click handling

to handle clicking, you need to call the `setOnMenuItemClickListener` method on the menu. 

```kotlin
      menu.setOnMenuItemClickListener { menuItem ->
          Snackbar.make(this, view, menuItem.title !!, 1000).show()
      }
```

## Support Libraries

For displaying a blur as a menu background, I use a great library [RealtimeBlurView](https://github.com/mmin18/RealtimeBlurView)
