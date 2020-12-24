import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {PageEvent} from "@angular/material/paginator";
import {CompoundFilter} from "./compound-filter.model";
import {Subscription} from "rxjs";
import {CompoundManageService} from "../../../../services/compound-manage.service";
import {OuterComponent} from "../../../../models/outer.model";
import {Router} from "@angular/router";

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
              private router: Router
  ) { }

  ngOnInit(): void {
    this.searchForm = this.formBuilder.group({
      search: ['', []]
    })
    this.searchByFilter();
  }

  searchByFilter(): void {
    this.subscription = this.compoundService.getAllCompoundsWithFilter(this.compoundFilter).subscribe(data =>
    {
      this.librariesCount = data["totalItems"];
      this.dataSource = data["list"];
    });
  }

  goToView(id): void {
    if (id) {
      this.router.navigate([`libraries-menu/compounds/${id}`]).then();
    }
  }

  getLibrariesCount(): void {
    this.subscription = this.compoundService.getAllCompounds().subscribe( data => {
      this.librariesCount = data["totalItems"];
    });
  }

  deleteCompound(id): void {
    this.subscription = this.compoundService.deleteCompound(id).subscribe(data =>{
      if (data) {
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

  ngOnDestroy() : void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}
