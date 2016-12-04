/*
*  HTTP Switch
*  Category: Device Handler
*
*  Source: https://community.smartthings.com/t/beta-release-uri-switch-device-handler-for-controlling-items-via-http-calls/37842
*
*  Credit: tguerena and surge919
*/

import groovy.json.JsonSlurper

metadata {
	definition (name: "Z-Wave Garage HTTP", namespace: "jasdf", author: "Jasdf") {
		capability "Contact Sensor"
		capability "Sensor"
		capability "Battery"
		capability "Configuration"

		capability "Actuator"
		capability "Door Control"
    capability "Garage Door Control"
		//capability "Refresh"

		fingerprint deviceId: "0x2001", inClusters: "0x30,0x80,0x84,0x85,0x86,0x72"
		fingerprint deviceId: "0x07", inClusters: "0x30"
		fingerprint deviceId: "0x0701", inClusters: "0x5E,0x98"
		fingerprint deviceId: "0x0701", inClusters: "0x5E,0x86,0x72,0x98", outClusters: "0x5A,0x82"
		fingerprint deviceId: "0x0701", inClusters: "0x5E,0x80,0x71,0x85,0x70,0x72,0x86,0x30,0x31,0x84,0x59,0x73,0x5A,0x8F,0x98,0x7A", outClusters:"0x20" // Philio multi+

  //	capability "Switch"
//		attribute "triggerswitch", "string"
//		command "DeviceTrigger"
	}

	preferences {
    }


	// simulator metadata
	simulator {
	}

	// UI tile definitions
	tiles {
		//
  	// standardTile("DeviceTrigger", "device.triggerswitch", width: 2, height: 2, canChangeIcon: true) {
		// 	state "triggeroff", label: 'Off', action: "on", icon: "st.switches.switch.off", backgroundColor: "#ffffff", nextState: "on"
		// 	state "triggeron", label: 'On', action: "off", icon: "st.switches.switch.on", backgroundColor: "#79b821", nextState: "off"
		// }
		// main "DeviceTrigger"
		// 	details (["DeviceTrigger"])

		standardTile("toggle", "device.door", width: 2, height: 2) {
			state("closed", label:'${name}', action:"door control.open", icon:"st.doors.garage.garage-closed", backgroundColor:"#79b821", nextState:"opening")
			state("open", label:'${name}', action:"door control.close", icon:"st.doors.garage.garage-open", backgroundColor:"#ffa81e", nextState:"closing")
			state("opening", label:'${name}', icon:"st.doors.garage.garage-closed", backgroundColor:"#ffe71e")
			state("closing", label:'${name}', icon:"st.doors.garage.garage-open", backgroundColor:"#ffe71e")

		}
		standardTile("open", "device.door", inactiveLabel: false, decoration: "flat") {
			state "default", label:'open', action:"door control.open", icon:"st.doors.garage.garage-opening"
		}
		standardTile("close", "device.door", inactiveLabel: false, decoration: "flat") {
			state "default", label:'close', action:"door control.close", icon:"st.doors.garage.garage-closing"
		}

		valueTile("battery", "device.battery", inactiveLabel: false, decoration: "flat") {
			state "battery", label:'${currentValue}% battery', unit:""
		}

		main "toggle"
		details(["toggle", "open", "close","battery"])
	}
}

// def on(evt) {
// 	log.debug "---ON COMMAND--- ${evt}"
//   sendEvent(name: "triggerswitch", value: "triggeron", isStateChange: true)
//  	sendEvent(name: "switch", value: "on")
// }

// def off(evt) {
// 	log.debug "---OFF COMMAND--- ${evt}"
//     sendEvent(name: "triggerswitch", value: "triggeroff", isStateChange: true)
//     sendEvent(name: "switch", value: "off")
// }

def parse(evt) {
	log.debug("${evt}")
}

def open() {
	sendEvent(name: "door", value: "opening")
    runIn(6, finishOpening)
}

def close() {
    sendEvent(name: "door", value: "closing")
	runIn(6, finishClosing)
}

def finishOpening() {
    sendEvent(name: "door", value: "open")
    sendEvent(name: "contact", value: "open")
}

def finishClosing() {
    sendEvent(name: "door", value: "closed")
    sendEvent(name: "contact", value: "closed")
}
