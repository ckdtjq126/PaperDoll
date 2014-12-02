General Specifications
1. I couldn't fix the 25pixel bug, So a click prior to demo is required.
2. I used anti-aliasing to make the edges of ellipses & rectangle smooth.
3. pressing 'q' simply closes the window (too lazy to click "File" and then "Quit").
4. pressing "Ctrl - R" resets the location of the body parts.
5. I added anti-alising to make the edges of the model smooth.

Drag
1. holding mouse down and dragging the body moves the whole body.

Rotate
1. all body parts rotate moving mouse x-direction (excluding Torso and Feet).
2. holding mouse and move -x direction cause 
	-> rotate the body part clockwise.
3. holding mouse and move +x direction
	-> rotate the body part counter-clockwise.
4. Feet use y-direction to perform rotation.

Scale
1. scaling uses y-direction.
1-1. moving +y direction causes the image to shrink (moving up).
1-2. moving -y direction causes the image to enlarge (moving down).
2. scaling either UpperLeg cause all four legs to scale.
3. scaling either LowerLeg cause other LowerLeg to scale.
4. when scaling UpperLegs and LowerLegs 
	(as long as they vertically, feet stays the same size).
4-1. when scaling UpperLegs and LowerLegs, feet size goes unstable.

Script
1. while recording or replaying the script, the reset function is disabled.
2. when you execute my files for the first time, you can go "Scripting" -> "Start script".
	It will replay the demo I have recorded to perform all the requirements:
		drag, rotation within its limit, scaling.
3. while recording/playing script, you can exit by clicking "File" -> "Quit" or pressing 'q'.

