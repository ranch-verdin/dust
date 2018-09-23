// CroneEngine_TestSine
// dumbest possible test: a single, mono sinewave

// Inherit methods from CroneEngine
Engine_TestSine : CroneEngine {
	// Define a getter for the synth variable
	var <synth;

	// Define a class method when an object is created
	*new { arg context, doneCallback;
		// Return the object from the superclass (CroneEngine) .new method
		^super.new(context, doneCallback);
	}
	// Rather than defining a SynthDef, use a shorthand to allocate a function and send it to the engine to play
	alloc {
		// Define the synth variable, whichis a function
		synth = {
			// define arguments to the function
			arg out, hz=220, amp=0.5, amplag=0.02, hzlag=0.01;
			// initialize two local vars for Lag'd amp and hz
			var amp_, hz_;
			// Allow Lag (Slew in modular jargon) for amplitude and frequency
			amp_ = Lag.ar(K2A.ar(amp), amplag);
			hz_ = Lag.ar(K2A.ar(hz), hzlag);
			// Create an output object with two copies of a SineOsc,
			// passing the Lag'd amp and frequency as args
			Out.ar(out, (SinOsc.ar(hz_) * amp_).dup);
		// Send the function to the engine as a UGen graph
		// pass the superclass's context method "out_b" as the value to the \out argument
		// pass the superclass's context context method "xg" as the value to the target
		// TODO What is a target and a context?
		}.play(args: [\out, context.out_b], target: context.xg);

		// Export argument symbols as modulatable paramaters
		// This could be extended to control the Lag time as additional params
		this.addCommand("hz", "f", { arg msg;
			synth.set(\hz, msg[1]);
		});

		this.addCommand("amp", "f", { arg msg;
			synth.set(\amp, msg[1]);
		});
	}
	// define a function that is called when the synth is shut down
	free {
		synth.free;
	}
}
