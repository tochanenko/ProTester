import {AfterViewInit, Component, Input, OnInit, ViewChild} from '@angular/core';
import {Action} from "../../actions/action.model";
import {ActionService} from "../../services/action/action.service";
import {ACTIONS} from "../../mock-actions";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";

@Component({
  selector: 'app-manage-action',
  templateUrl: './manage-action.component.html',
  styleUrls: ['./manage-action.component.css']
})
export class ManageActionComponent implements OnInit,AfterViewInit {

  selectedAction: Action[] = [];

  actions = ACTIONS;

  displayedColumns: string[] = ['name', 'description', 'type','actions'];

  image = 'assets/logo.png';

  dataSource = new MatTableDataSource<Action>(this.actions);


  constructor(private actionService: ActionService){}

  @ViewChild(MatPaginator) paginator: MatPaginator;
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  ngOnInit(): void {
  }


  onSaveClicked(action: any){
    this.selectedAction.push(action);
  }


  saveAction(action: Action) {
    console.log("Params"+action.preparedParams);
  }
}
