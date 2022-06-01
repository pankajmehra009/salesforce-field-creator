'use strict'

angular.module('demo.services', []).factory('UserService',
		[ "$http", "CONSTANTS", function($http, CONSTANTS) {
			var service = {};
			console.log(">>");
			service.getUserById = function(userId) {
				var url = CONSTANTS.getUserByIdUrl + userId;
				return $http.get(url);
			}
			service.getAllUsers = function() {
				return $http.get(CONSTANTS.getAllUsers);
			}
			service.getAccessCode = function() {
				return $http.get(CONSTANTS.getAccessCode);
			}
			service.saveUser = function(userDto) {
				return $http.post(CONSTANTS.saveUser, userDto);
			}
			service.getsObjects = function(code,domain) {
				return $http.get(CONSTANTS.getsObjects + "?code="  + code + "&domain="  + domain ); 
			}
			service.createField = function(fieldDto) {
				return $http.post(CONSTANTS.createField , fieldDto); 
			}
			service.deletesession = function() {
				return $http.get(CONSTANTS.deletesession); 
			}
			service.createLead = function(leadDto) {
				return $http.post(CONSTANTS.createLead , leadDto); 
			}
			return service;
		} ]);