import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {PageEvent} from "@angular/material/paginator";
import {CompoundFilter} from "./compound-filter.model";
import {Subscription} from "rxjs";
import {CompoundManageService} from "../../../../services/compound-manage.service";
import {OuterComponent} from "../../../../models/outer.model";
import {Router} from "@angular/router";
import {DialogUtilComponent} from "../../../../components/dialog-util/dialog-util.component";
import {MatDialog} from "@angular/material/dialog";
import {DialogWarningModel} from "../../../../models/dialog-warning.model";

@Component({
  selector: 'app-compound-search',
  templateUrl: './compound-search.component.html',
  styleUrls: ['./compound-search.component.css']
})
export class SearchComponent implements OnInit {
  searchForm: FormGroup;
  dataSource: OuterComponent[];
  pageEvent: PageEvent;

  columnsToDisplay: string[] = ['NAME', 'DESCRIPTION', 'CONF'];
  pageSizeOptions: number[] = [5, 10, 25, 50];
  librariesCount = 10;
  compoundFilter: CompoundFilter = new CompoundFilter();

  private subscription: Subscription;

  constructor(private formBuilder: FormBuilder,
              private compoundService: CompoundManageService,
              private router: Router,
              private dialog: MatDialog
  ) {
  }

  ngOnInit(): void {
    this.searchForm = this.formBuilder.group({
      search: ['', []]
    })
    this.searchByFilter();
  }

  searchByFilter(): void {
    this.subscription = this.compoundService.getAllCompoundsWithFilter(this.compoundFilter).subscribe(data => {
      this.librariesCount = data["totalItems"];
      data["list"].forEach(item => item.name = this.parseDescription(item.name));
      console.log(data["list"]);
      this.dataSource = data["list"];
    });
  }

  goToView(id): void {
    if (id) {
      this.router.navigate([`libraries-menu/compounds/${id}`]).then();
    }
  }

  goToEdit(id): void {
    if (id) {
      this.router.navigate([`libraries-menu/compounds/${id}/edit`]).then();
    }
  }

  openDialogDelete(id): void {
    this.compoundService.deleteCompound(id).subscribe((data) => {

      }, error => {
      console.log(error);
      const warning: DialogWarningModel = {
        error_name: error.error.message,
        message: '',
        links: error.error.outerComponents.map(component =>  `/libraries-menu/compounds/${component.id}` )
      }
        const dialogRef = this.dialog.open(DialogUtilComponent, {
          width: '350px',
          data: warning
        });

        dialogRef.afterClosed().subscribe(result => {
          console.log('The dialog was closed');
        });
      }
    );
  }

  getLibrariesCount(): void {
    this.subscription = this.compoundService.getAllCompounds().subscribe(data => {
      this.librariesCount = data["totalItems"];
    });
  }

  deleteCompound(id): void {
    this.subscription = this.compoundService.deleteCompound(id).subscribe(data => {
      if (data) {
        console.log("Successful delete!")
        this.searchByFilter();
      }
    }, error => console.error(error.error.message));
  }

  onPaginateChange(event): void {
    this.compoundFilter.pageNumber = event.pageIndex + 1;
    this.compoundFilter.pageSize = event.pageSize;

    this.searchByFilter();
  }


  get f() {
    return this.searchForm.controls;
  }

  onFormBlurs(): void {
    this.compoundFilter.compoundName = this.f.search.value;
    this.searchByFilter();
  }

  parseDescription(description: string | Object) {
    if (typeof description !== "object") {
      const regexp = new RegExp('(\\$\\{.+?\\})');
      let splitted = description.split(regexp);
      return splitted.map(sub_string => {
        if (sub_string.includes("${")) {
          return {
            text: sub_string.replace('${', '').replace('}', ''),
            input: true
          }
        } else {
          return {
            text: sub_string,
            input: false
          }
        }
      })
    } else {
      return description;
    }
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}
