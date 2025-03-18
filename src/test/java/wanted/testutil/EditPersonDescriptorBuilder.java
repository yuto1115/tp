package wanted.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import wanted.logic.commands.EditCommand.EditPersonDescriptor;
import wanted.model.loan.Amount;
import wanted.model.loan.Loan;
import wanted.model.loan.LoanDate;
import wanted.model.loan.Name;
import wanted.model.tag.Tag;

/**
 * A utility class to help with building EditPersonDescriptor objects.
 * This is disabled in the MVP
 */
public class EditPersonDescriptorBuilder {

    private EditPersonDescriptor descriptor;

    public EditPersonDescriptorBuilder() {
        descriptor = new EditPersonDescriptor();
    }

    public EditPersonDescriptorBuilder(EditPersonDescriptor descriptor) {
        this.descriptor = new EditPersonDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPersonDescriptor} with fields containing {@code loan}'s details
     */
    public EditPersonDescriptorBuilder(Loan person) {
        descriptor = new EditPersonDescriptor();
        descriptor.setName(person.getName());
        descriptor.setAmount(person.getAmount());
        descriptor.setDate(person.getLoanDate());
        descriptor.setTags(person.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }
    /**
     * Sets the {@code Amount} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withAmount(String amount) {
        descriptor.setAmount(new Amount(amount));
        return this;
    }

    /**
     * Sets the {@code LoanDate} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withLoanDate(String date) {
        descriptor.setDate(new LoanDate(date));
        return this;
    }
    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditPersonDescriptor}
     * that we are building.
     */
    public EditPersonDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditPersonDescriptor build() {
        return descriptor;
    }
}
