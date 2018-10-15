(function() {

    var app = angular.module('SchoolEditor', [
            'ngResource', 
            'ui.grid', 
            'ui.grid.edit', 
            'ui.grid.rowEdit', 
            'ui.grid.cellNav', 
            'ui.grid.pagination', 
            'ui.select', 
            'ngDialog'
    ]);

    app
    .controller('SchoolGridController', [
            '$scope', '$http', 'uiGridConstants', 'SchoolType', 'SchoolPriority', function($scope, $http, uiGridConstants, SchoolType, SchoolPriority) {
                $scope.msg = '';
                $scope.schooList = [];
                $scope.schoolTypes = [];
                $scope.schoolPriorities = [];

                var paginationOptions = {
                    pageNumber : 1,
                    pageSize : 25,
                    sort : 'name'
                };

                SchoolType.query().$promise.then(function(data) {
                    $scope.schoolTypes = data._embedded.schoolTypes;
                    var lookupSchoolTypes = [];
                    var currentEntry = {};
                    for (var i = 0; $scope.schoolTypes.length; i++) {
                        currentEntry = $scope.schoolTypes[i];
                        var item = {
                            "id" : currentEntry.id,
                            "value" : currentEntry
                        };
                        lookupSchoolTypes.push(item);
                    }
                    ;
                    $scope.gridOptions.columnDefs[2].editDropdownOptionsArray = lookupSchoolTypes;
                    console.log("Retrieve School Types")
                }, function(error) {
                    $scope.schoolTypes = [];
                });

                SchoolPriority.query().$promise.then(function(data) {
                    $scope.schoolPriorities = data._embedded.schoolPriorities;
                    $scope.gridOptions.columnDefs[3].editDropdownOptionsArray = $scope.schoolPriorities;
                    console.log("Retrieve School Priorities")
                }, function(error) {
                    $scope.schoolPriorities = [];
                });

                $scope.gridOptions = {
                    enableCellEditOnFocus : true,
                    enablePagination : true,
                    enablePaginationControls : false,
                    useExternalPagination : true,
                    useExternalSorting : true,
                    paginationPageSizes : [
                            25, 50, 100
                    ],
                    paginationPageSize : 25,
                    columnDefs : [
                            {
                                field : 'id',
                                enableCellEdit : false,
                                width : 60
                            }, {
                                field : 'name',
                                enableCellEdit : true,
                                displayName : 'School Name'
                            }, {
                                field : 'type',
                                displayName : 'Type',
                                enableCellEdit : true,
                                enableSorting : true,
                                cellFilter : 'mapSchoolType',
                                editDropdownIdLabel : 'id',
                                editDropdownValueLabel : 'code',
                                editableCellTemplate : 'ui-grid/dropdownEditor',
                                // editableCellTemplate :
                                // '/views/school-type.html',
                                editDropdownOptionsArray : []
                            }, {
                                field : 'priority',
                                displayName : 'Priority',
                                enableCellEdit : true,
                                enableSorting : true,
                                cellFilter : 'mapSchoolPriority',
                                editDropdownIdLabel : 'field.id',
                                editDropdownValueLabel : 'field.code',
                                editableCellTemplate : 'ui-grid/dropdownEditor',
                                editDropdownOptionsArray : []
                            }
                    ]
                };

                var getPage = function() {
                    var url = '/schools/';
                    url += '?size=' + paginationOptions.pageSize;
                    url += '&page=' + paginationOptions.pageNumber;

                    url += '&sort=' + paginationOptions.sort;
                    switch (paginationOptions.direction) {
                        case uiGridConstants.ASC:
                            url += ',asc';
                            break;
                        case uiGridConstants.DESC:
                            url += ',desc';
                            break;
                        default:
                            break;
                    }

                    $http.get(url).success(function(data) {
                        $scope.schooList = data._embedded.schools;
                        $scope.gridOptions.data = $scope.schooList;
                        $scope.gridOptions.totalItems = data.page.totalElements;
                    });

                };

                $scope.saveRow = function(rowEntity) {
                    console.log("Attemtping Save");
                    // create a fake promise - normally you'd use the promise
                    // returned by $http or $resource
                    var promise = $q.defer();
                    $scope.gridApi.rowEdit.setSavePromise(rowEntity, promise.promise);

                    // fake a delay of 3 seconds whilst the save occurs, return
                    // error if gender is "male"
                    $interval(function() {
                        if (rowEntity.gender === 'male') {
                            promise.reject();
                        } else {
                            promise.resolve();
                        }
                    }, 3000, 1);
                };
                $scope.currentFocused = "";

                $scope.getCurrentFocus = function() {
                    var rowCol = $scope.gridApi.cellNav.getFocusedCell();
                    if (rowCol !== null) {
                        $scope.currentFocused = 'Row Id:' + rowCol.row.entity.id + ' col:' + JSON.stringify(rowCol.row.entity);
                    }
                }

                $scope.gridOptions.onRegisterApi = function(gridApi) {
                    $scope.gridApi = gridApi;
                    $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
                        if (sortColumns.length == 0) {
                            paginationOptions.sort = sortCoulmn[0].field;
                            paginationOptions.direction = sortCoulmn[0].column;

                        } else {
                            paginationOptions.sort = sortColumns[0].field;
                            paginationOptions.direction = sortColumns[0].sort.direction;
                        }
                        getPage();
                    });
                    $scope.gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize) {
                        paginationOptions.pageNumber = newPage;
                        paginationOptions.pageSize = pageSize;
                        getPage();
                    });
                    $scope.gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
                        switch (colDef.field) {
                            case 'type':
                                break;
                            case 'priority':
                                break;
                        }
                        $scope.msg.lastCellEdited = 'edited row id:' + rowEntity.id + ' Column:' + colDef.name + ' newValue:' + newValue + ' oldValue:' + oldValue;
                        $scope.$apply();
                    });
                };

                getPage();

            }
    ]);


})();
