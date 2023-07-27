package xd.metamath;

public class mmmaci {







#include <string.h>





#ifdef THINK_C

#include "mmmaci.h"

#define kBaseResID 128
#define kMoveToFront (WindowPtr)-1L

#define kHorizontalPixel 30
#define kVerticalPixel 50




void ToolBoxInit(void)
{
  InitGraf(&thePort);
  InitFonts();
  InitWindows();
  InitMenus();
  TEInit();
  InitDialogs(nil);
  InitCursor();
}


void WindowInit(void)
{
  WindowPtr window;
  window = GetNewWindow(kBaseResID, nil, kMoveToFront);
  if (window == nil)
  {
    SysBeep(10);
    ExitToShell();
  }

  ShowWindow(window);
  SetPort(window);


}


void DrawMyPicture(void)
{
  Rect pictureRect;
  WindowPtr window;
  PicHandle picture;

  window = FrontWindow();
  pictureRect = window->portRect;
  picture = GetPicture(kBaseResID);

  if (picture == nil) {
    SysBeep(10);
    ExitToShell();
  }

  CenterPict(picture, &pictureRect);
  DrawPicture(picture, &pictureRect);
}


void CenterPict(PicHandle picture, Rect *destRectPtr)
{
  Rect windRect, pictRect;
  windRect = *destRectPtr;
  pictRect = (**(picture)).picFrame;
  OffsetRect(&pictRect, windRect.left - pictRect.left,
      windRect.top - pictRect.top);
  OffsetRect(&pictRect, (windRect.right - pictRect.right)/2,
     (windRect.bottom - pictRect.bottom)/2);
  *destRectPtr = pictRect;
}

#endif
}