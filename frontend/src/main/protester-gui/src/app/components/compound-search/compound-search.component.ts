import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {PageEvent} from "@angular/material/paginator";
import {LibraryFilter} from "../library-search/library-filter.model";
import {Subscription} from "rxjs";
import {CompoundManageService} from "../../services/compound-manage.service";
import {OuterComponent} from "../../models/outer.model";

@Component({
  selector: 'app-compound-search',
  templateUrl: './compound-search.component.html',
  styleUrls: ['./compound-search.component.css']
})
export class CompoundSearchComponent implements OnInit {
  searchForm: FormGroup;
  dataSource: OuterComponent[];
  pageEvent: PageEvent;

  columnsToDisplay: string[] = ['NAME', 'DESCRIPTION', 'CONF'];
  pageSizeOptions: number[] = [5, 10, 25, 50];
  librariesCount = 10;
  libraryFilter: LibraryFilter = new LibraryFilter();

  private subscription: Subscription;

  constructor(private formBuilder: FormBuilder, private compoundService: CompoundManageService) { }

  ngOnInit(): void {
    this.searchForm = this.formBuilder.group({
      search: ['', []]
    })
    this.searchByFilter();
    this.getLibrariesCount();
  }

  searchByFilter(): void {
    this.subscription = this.compoundService.getAllCompounds().subscribe(data =>
    {
      let filtered_compounds = data.filter(compound => compound.name.includes(this.libraryFilter.libraryName));
      this.librariesCount = filtered_compounds.length;

      let pageSize = this.libraryFilter.pageSize;
      let pageNumber = this.libraryFilter.pageNumber;
      let range = pageSize * pageNumber;
      this.dataSource = filtered_compounds.slice(range, pageSize + range);
    });
  }

  getLibrariesCount(): void {
    this.subscription = this.compoundService.getAllCompounds().subscribe( data => {
      this.librariesCount = data.length;
    });
  }

  onPaginateChange(event): void {
    this.libraryFilter.pageNumber = event.pageIndex;
    this.libraryFilter.pageSize = event.pageSize;

    this.searchByFilter();
  }


  get f() {
    return this.searchForm.controls;
  }

  onFormBlurs(): void {
    this.libraryFilter.libraryName = this.f.search.value;
    this.searchByFilter();
  }

  ngOnDestroy() : void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}
